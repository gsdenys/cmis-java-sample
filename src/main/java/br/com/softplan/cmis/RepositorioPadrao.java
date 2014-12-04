package br.com.softplan.cmis;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by denys.santos on 28/11/2014.
 */
public class RepositorioPadrao {

    // URL usada para acessar o servidor CMIS via AtomPub
    private final String CMIS_URL = "http://server2137:8180/alfresco/api/-default-/public/cmis/versions/1.1/atom";

    // Usuário e senha do usuário
    private final String CMIS_USER = "demo";
    private final String CMIS_PASSWORD = "demo";

    /**
     * Construtor padrão
     */
    public RepositorioPadrao() {

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

        System.out.println("Repositório Padrão: " + repository.getId());
    }

    /**
     * Metodo Principal
     *
     * @param args
     */
    public static void main(String args[]) {
        new RepositorioPadrao();
    }
}
