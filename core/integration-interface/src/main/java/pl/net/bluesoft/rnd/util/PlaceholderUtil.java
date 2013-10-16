package pl.net.bluesoft.rnd.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: POlszewski
 * Date: 2013-09-30
 * Time: 13:09
 */
public class PlaceholderUtil {
	private static final String REGEX = "#(\\{([^}]*)\\}|#)";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

	public interface ReplacementCallback {
		String getReplacement(String placeholderName);
	}

	public static String expand(String pattern, ReplacementCallback callback) {
		StringBuffer result = new StringBuffer(pattern.length());
		Matcher matcher = PATTERN.matcher(pattern);

		while (matcher.find()) {
			if (matcher.group(2) != null) {
				String placeholderName = matcher.group(2);
				String replacement = String.valueOf(callback.getReplacement(placeholderName));

				matcher.appendReplacement(result, replacement);
			}
			else {
				matcher.appendReplacement(result, matcher.group(1));
			}
		}
		matcher.appendTail(result);
		return result.toString();
	}

	public static Set<String> getUsedPlaceholderNames(String pattern) {
		Set<String> result = new HashSet<String>();
		Matcher matcher = PATTERN.matcher(pattern);

		while (matcher.find()) {
			if (matcher.group(2) != null) {
				result.add(matcher.group(2));
			}
		}
		return result;
	}

	public static Set<String> getUsedPlaceholderNames(Collection<String> values) {
		Set<String> result = new HashSet<String>();

		for (String pattern : values) {
			result.addAll(getUsedPlaceholderNames(pattern));
		}
		return result;
	}
}