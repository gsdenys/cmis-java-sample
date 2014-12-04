package br.com.softplan.cmis;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by denys.santos on 28/11/2014.
 */
public class PegaPasta {

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
    public PegaPasta() {

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
        //                                        CRIA SESSÃO
        //----------------------------------------------------------------------------------------------------

        //seta repositório para ser acessível pela conexão
        parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());

        //pega sessão
        Session session =  factory.createSession(parameter);

        //----------------------------------------------------------------------------------------------------
        //                    PEGA PASTA QUE SERÁ USADA COMO BASE PARA TODOS OS TESTES
        //----------------------------------------------------------------------------------------------------

        //Pega pasta com base no ID
        CmisObject cmisObject = session.getObject(FOLDER_ID);
        Folder folder = (Folder) cmisObject;

        System.out.println("Pasta: " + folder.getPath());
    }

    /**
     * Metodo Principal
     *
     * @param args
     */
    public static void main(String args[]) {
        new PegaPasta();
    }
}
