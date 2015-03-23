package br.com.softplan.cmis;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.PropertyHtmlDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by denys.santos on 28/11/2014.
 */
public class DiscovetyTypes {

    // URL usada para acessar o servidor CMIS via AtomPub
    private final String CMIS_URL = "http://server2137:8180/alfresco/api/-default-/public/cmis/versions/1.1/atom";

    // Usuário e senha do usuário
    private final String CMIS_USER = "demo";
    private final String CMIS_PASSWORD = "demo";

    /**
     * Construtor padrão
     */
    public DiscovetyTypes() {

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
        //                                         CRIA SESSÃO
        //----------------------------------------------------------------------------------------------------

        //seta repositório para ser acessível pela conexão
        parameter.put(SessionParameter.REPOSITORY_ID, repository.getId());

        //pega sessão
        Session session =  factory.createSession(parameter);

        System.out.println("Sessão: " + session);

        //----------------------------------------------------------------------------------------------------
        //                                DESCOBRE OS TIPOS DOCUMENTAIS
        //----------------------------------------------------------------------------------------------------

       boolean includePropertyDefintions = true;
       List<Tree<ObjectType>> arvore = session.getTypeDescendants(
                null, //Se este parâmetro for null inicia no topo da arvore de tipos
                -1, //infinitas interações de buscas
                includePropertyDefintions //inclui definição de propriedades
       );

       for(Tree<ObjectType> galho : arvore) {
           printTypes(galho, "");
       }
    }



    void printTypes(Tree<ObjectType> tree, String tab){
        ObjectType objType = tree.getItem();
        System.out.println(tab + "TYPE:" + objType.getDisplayName() + " (" + objType.getDescription() + ", ID: " + objType.getId() + ")");

        Map<String, PropertyDefinition<?>> propDef = objType.getPropertyDefinitions();
        Set<String> keySet = propDef.keySet();

        for (String key : keySet){
            PropertyDefinition<?> def = propDef.get(key);
            System.out.println( tab + "  PROP: " + def.getDisplayName() + "(" + def.getDescription() + ", ID: " + def.getId() +  ")");

        }


        for (Tree<ObjectType> t : tree.getChildren()) {
            // there are more - call self for next level
            printTypes(t, tab + " ");
        }
    }


    /**
     * Metodo Principal
     *
     * @param args
     */
    public static void main(String args[]) {
        new DiscovetyTypes();
    }
}
