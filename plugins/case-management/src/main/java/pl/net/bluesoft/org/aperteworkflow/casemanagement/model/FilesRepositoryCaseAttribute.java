package pl.net.bluesoft.org.aperteworkflow.casemanagement.model;

import org.aperteworkflow.files.model.FilesRepositoryItem;
import org.aperteworkflow.files.model.IFilesRepositoryAttribute;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pkuciapski on 2014-05-13.
 */
@Entity
@Table(name = "pt_case_files_attr")
public class FilesRepositoryCaseAttribute extends CaseAttribute implements IFilesRepositoryAttribute {
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "case_file_id")
    @Index(name = "idx_pt_files_case_file_id")
    private Set<FilesRepositoryItem> filesRepositoryItems = new HashSet<FilesRepositoryItem>();

    @Override
    public Set<FilesRepositoryItem> getFilesRepositoryItems() {
        return filesRepositoryItems;
    }

    @Override
    public String getParentObjectPropertyName() {
        return CASE_INSTANCE_ID;
    }

    @Override
    public Long getParentObjectId() {
        return getCase().getId();
    }

    @Override
    public void removeItem(FilesRepositoryItem item) {
        getFilesRepositoryItems().remove(item);
    }

    public void setFilesRepositoryItems(Set<FilesRepositoryItem> filesRepositoryItems) {
        this.filesRepositoryItems = filesRepositoryItems;
    }
}
