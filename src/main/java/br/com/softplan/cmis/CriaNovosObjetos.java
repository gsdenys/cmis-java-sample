package br.com.softplan.cmis;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by denys.santos on 28/11/2014.
 */
public class CriaNovosObjetos {

    // URL usada para acessar o servidor CMIS via AtomPub
    private final String CMIS_URL = "http://server2137:8180/alfresco/api/-default-/public/cmis/versions/1.1/atom";

    // Usuário e senha do usuário
    private final String CMIS_USER = "demo";
    private final String CMIS_PASSWORD = "demo";

    // ID da pasta que será usuda para testar os comandos CMIS
    private final String FOLDER_ID = "69d92d45-42ac-493b-a230-d176ca73496e";

    /**
     * Construtor padrão
     */
    public CriaNovosObjetos() {

        //----------------------------------------------------------------------------------------------------
        //                                   PEGA REPOSITÓRIO PADRÃO
        //----------------------------------------------------------------------------------------------------

        // Instanciação padrão do Factory OpenCMIS
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        //Credenciais de usuários
        parameter.put(SessionParameter.USER, CMIS_USER);
        parameter.put(SessionParameter.PASSWORD, CMIS_PASSWORD);

        //Parâmetros de conexão
        parameter.put(SessionParameter.ATOMPUB_URL, CMIS_URL);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        //Pega os repositórios existentes – O primeiro da lista é o padrão
        List<Repository> repositories = factory.getRepositories(parameter);
        Repository repository = repositories.get(0);

        //----------------------------------------------------------------------------------------------------
        //                                   CRIA SESSÃO
        //----------------------------------------------------------------------------------------------------

        //seta repositório para ser acessível pela conexão
        parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());

        //pega sessão
        Session session =  factory.createSession(parameter);

        //----------------------------------------------------------------------------------------------------
        //                    PEGA PASTA QUE SERÁ USADA COMO BASE PARA TODOS OS TESTES
        //----------------------------------------------------------------------------------------------------

        //pega pasta para servir como base para os testes
        CmisObject cmisObject = session.getObject(FOLDER_ID);
        Folder folder = (Folder) cmisObject;


        //----------------------------------------------------------------------------------------------------
        //                            CRIA NOVA PASTA DENTRO DA PASTA PADRÃO
        //----------------------------------------------------------------------------------------------------

        //cria as propriedades para a pasta a ser criada
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        properties.put(PropertyIds.NAME, "teste-" + System.currentTimeMillis());

        //cria a nova pasta
        Folder newFolder = folder.createFolder(properties);

        //----------------------------------------------------------------------------------------------------
        //                      CRIA NOVO DOCUMENTO DENTRO DA PASTA CRIADA ACIMA
        //----------------------------------------------------------------------------------------------------

        //cria as propriedades para O DOCUMENTO a ser criado
        String name = "teste-" + System.currentTimeMillis() + ".txt";
        Map<String, Object> docProperties = new HashMap<String, Object>();
        docProperties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        docProperties.put(PropertyIds.NAME, name);

        // Cria conteúdo para o documento
        String fileContent = "Novo doc - " + System.currentTimeMillis();
        byte[] content = fileContent.getBytes();
        InputStream stream = new ByteArrayInputStream(content);
        BigInteger contentLength = BigInteger.valueOf(content.length);
        ContentStream contentStream = new ContentStreamImpl(name, contentLength, "cmis:document", stream);

        //cria o documento
        newFolder.createDocument(docProperties, contentStream, VersioningState.MAJOR);
    }

    /**
     * Metodo Principal
     *
     * @param args
     */
    public static void main(String args[]) {
        new CriaNovosObjetos();
    }
}
