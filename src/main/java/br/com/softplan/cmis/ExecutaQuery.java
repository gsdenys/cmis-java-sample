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
import java.util.*;

/**
 * Created by denys.santos on 28/11/2014.
 */
public class ExecutaQuery {

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
    public ExecutaQuery() {
        //----------------------------------------------------------------------------------------------------
        //                                           CRIA SESSÃO
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

        //seta repositório para ser acessível pela conexão
        parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());

        //pega sessão
        Session session =  factory.createSession(parameter);

        //----------------------------------------------------------------------------------------------------
        //                                           EXECUTA QUERY
        //----------------------------------------------------------------------------------------------------

        //Query - Lista todas as pastas
        String queryAllFolders = "select * from cmis:folder";
        //Query - Lista documentos criados após 02/12/2014
        String queryDoc = "Select * from cmis:document where cmis:creationDate > TIMESTAMP '2014-12-02T00:00:00.000-03:00'";

        ItemIterable<QueryResult> folderItems = session.query(queryAllFolders, false);
        this.print(folderItems, "Todas as Pastas");

        ItemIterable<QueryResult> docItems = session.query(queryDoc, false);
        this.print(docItems, "Documentos criados após 02/12/2014");
    }


    /**
     * Imprime o resultado da pesquisa
     *
     * @param folderItems
     * @param label
     */
    private void print(ItemIterable<QueryResult> folderItems, String label){
        System.out.println(label);
        for(QueryResult result : folderItems) {
            System.out.println("\t" + result.getPropertyById("cmis:name").getValues().get(0));

            //Le todas as propriedades do item e prepara para a apresentação
            for(PropertyData propertyData : result.getProperties()){
                System.out.println("\t\t" + propertyData.getDisplayName() + ": " + propertyData.getValues());
            }
        }
    }

    /**
     * Metodo Principal
     *
     * @param args
     */
    public static void main(String args[]) {
        new ExecutaQuery();
    }
}
