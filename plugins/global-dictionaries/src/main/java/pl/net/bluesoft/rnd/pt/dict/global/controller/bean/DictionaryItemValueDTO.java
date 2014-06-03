package pl.net.bluesoft.rnd.pt.dict.global.controller.bean;

import pl.net.bluesoft.rnd.processtool.dict.DictionaryItem;
import pl.net.bluesoft.rnd.processtool.model.dict.ProcessDictionaryItemExtension;
import pl.net.bluesoft.rnd.processtool.model.dict.db.ProcessDBDictionaryItemExtension;
import pl.net.bluesoft.rnd.processtool.model.dict.db.ProcessDBDictionaryItemValue;
import pl.net.bluesoft.rnd.util.i18n.I18NSource;
import pl.net.bluesoft.util.lang.FormatUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by pkuciapski on 2014-06-02.
 */
public class DictionaryItemValueDTO {
    private String id;
    private String value;
    private String dateFrom;
    private String dateTo;
    private Collection<DictionaryItemExtDTO> extensions = new ArrayList<DictionaryItemExtDTO>();
    private Boolean toDelete = Boolean.FALSE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Collection<DictionaryItemExtDTO> getExtensions() {
        return extensions;
    }

    public void setExtensions(Collection<DictionaryItemExtDTO> extensions) {
        this.extensions = extensions;
    }

    public static DictionaryItemValueDTO createFrom(ProcessDBDictionaryItemValue value, I18NSource messageSource) {
        DictionaryItemValueDTO dto = new DictionaryItemValueDTO();
        dto.setId(String.valueOf(value.getId()));
        dto.setValue(value.getValue(messageSource.getLocale()));
        dto.setDateFrom(FormatUtil.formatShortDate(value.getValidFrom()));
        dto.setDateTo(FormatUtil.formatShortDate(value.getValidTo()));
        for (ProcessDBDictionaryItemExtension ext : value.getExtensions()) {
            DictionaryItemExtDTO extDTO = DictionaryItemExtDTO.createFrom(ext, messageSource);
            dto.getExtensions().add(extDTO);
        }
        return dto;
    }

    public ProcessDBDictionaryItemValue toProcessDBDictionaryItemValue(String languageCode) {
        final ProcessDBDictionaryItemValue value = new ProcessDBDictionaryItemValue();
        if (this.getId() != null && !"".equals(this.getId()))
            value.setId(Long.valueOf(this.getId()));
        updateValue(value, languageCode);
        return value;
    }

    public void updateValue(ProcessDBDictionaryItemValue value, String languageCode) {
        value.setValue(languageCode, this.getValue());
        if (this.getDateFrom() != null && !"".equals(this.getDateFrom()))
            value.setValidFrom(FormatUtil.parseDate("yyyy-MM-dd", this.getDateFrom()));
        if (this.getDateTo() != null && !"".equals(this.getDateTo()))
            value.setValidTo(FormatUtil.parseDate("yyyy-MM-dd", this.getDateTo()));
    }

    public Boolean getToDelete() {
        return toDelete;
    }

    public void setToDelete(Boolean toDelete) {
        this.toDelete = toDelete;
    }
}
