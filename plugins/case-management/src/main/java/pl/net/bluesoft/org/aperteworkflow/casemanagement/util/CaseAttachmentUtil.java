package pl.net.bluesoft.org.aperteworkflow.casemanagement.util;

import org.aperteworkflow.files.model.FilesRepositoryItem;
import org.aperteworkflow.files.model.IFilesRepositoryItem;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.CaseStage;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.CaseStageAttributes;
import pl.net.bluesoft.util.lang.Lang;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: POlszewski
 * Date: 2014-09-23
 */
public class CaseAttachmentUtil {
	private static final Logger logger = Logger.getLogger(CaseAttachmentUtil.class.getName());

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static void addStageFile(CaseStage stage, IFilesRepositoryItem item) {
		addStageFiles(stage, Collections.singleton(item));
	}

	public static void addStageFiles(CaseStage stage, Collection<? extends IFilesRepositoryItem> filesList) {
		Collection<IFilesRepositoryItem> stageFiles = getFiles(stage);
		if (stageFiles == null) {
			stageFiles = new ArrayList<IFilesRepositoryItem>();
		}
		stageFiles.addAll(filesList);
		setFiles(stage, stageFiles);
	}

	private static Collection<IFilesRepositoryItem> getFiles(CaseStage stage) {
		String stageFilesJson = getStageFilesJson(stage);
		Collection<IFilesRepositoryItem> stageFiles = null;

		if (stageFilesJson != null) {
			try {
				stageFiles = MAPPER.readValue(stageFilesJson, new TypeReference<List<FilesRepositoryItem>>() {});// todo unmarshal data
			}
			catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return stageFiles;
	}

	private static void setFiles(CaseStage stage, Collection<IFilesRepositoryItem> stageFiles) {
		try {
			String json = MAPPER.writeValueAsString(stageFiles);
			stage.setSimpleLargeAttribute(CaseStageAttributes.STAGE_FILES.value(), json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStageFilesJson(CaseStage stage) {
		return stage.getSimpleLargeAttributeValue(CaseStageAttributes.STAGE_FILES.value());
	}

	public static void removeStageFile(CaseStage stage, Long itemId) {
		Collection<IFilesRepositoryItem> stageFiles = getFiles(stage);
		for (Iterator<IFilesRepositoryItem> iterator = stageFiles.iterator(); iterator.hasNext(); ) {
			IFilesRepositoryItem stageFile = iterator.next();
			if (Lang.equals(stageFile.getId(), itemId)) {
				iterator.remove();
				setFiles(stage, stageFiles);
				break;
			}
		}
	}
}
