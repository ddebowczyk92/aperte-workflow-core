package pl.net.bluesoft.org.aperteworkflow.casemanagement.dao;

import org.aperteworkflow.files.dao.FilesRepositoryAttributeFactory;
import org.aperteworkflow.files.model.IFilesRepositoryAttribute;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.FilesRepositoryCaseAttribute;

/**
 * Created by pkuciapski on 2014-05-14.
 */
public class FilesRepositoryCaseAttributeFactoryImpl extends FilesRepositoryAttributeFactory {
    @Override
    public IFilesRepositoryAttribute create() {
        return new FilesRepositoryCaseAttribute();
    }
}
