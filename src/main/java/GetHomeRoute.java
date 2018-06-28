import spark.*;

import java.io.File;
import java.io.FileReader;
import java.util.Base64;
import java.util.Random;

public class GetHomeRoute implements Route {

    private Random random = new Random();

    @Override
    public Object handle(Request request, Response response) throws Exception {

        File html = new File("src/main/resources/index.html");
        System.out.println(html.getAbsolutePath());

        Session userSession = request.session();
        String ID = userSession.attribute("wsID");
        if(ID == null){
            ID = randomString();
            while(Chat.sessionIDs.containsKey(ID)){
                ID = randomString();
            }

            userSession.attribute("wsID", ID);
            System.out.println("Created user: " + ID);
        }

        String output = "";
        try(FileReader fr = new FileReader(html)){
            int i;
            while( (i=fr.read()) != -1 ){
                output += (char) i;
            }
        }
        output += "<script>var myID = '" + ID + "';</script>";
//        System.out.println(output);
        return output;
    }



    private String randomString(){
        byte[] rand = new byte[16];
        random.nextBytes(rand);
        return Base64.getEncoder().encodeToString(rand);
    }
}
