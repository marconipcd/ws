import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class testeSSH {

	public static void main(String[] args) {
		
		try{
				String username = "testeb";
				
				HttpURLConnection httpClient = (HttpURLConnection) new URL("http://localhost/ssh/index.php?funcao=informacoes&username="+username).openConnection();
				httpClient.setRequestMethod("GET");
				httpClient.setDoOutput(true);			        
		
		        try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
		
		            String line;
		            StringBuilder response = new StringBuilder();
		
		            while ((line = in.readLine()) != null) {
		                response.append(line);
		            }
		            
		            String[] res = response.toString().split("<br />");
		            
		            String uptime = res[56].split(":")[3]+":"+res[56].split(":")[4]+":"+res[56].split(":")[5];
		            String mac = res[17].split(":")[1];
		            String ip = res[18].split(":")[1];
		            String upload = res[89].split(":")[1];
		            String download = res[94].split(":")[1];
		            		            
		
		            System.out.println(res);
		            //System.out.println(response.toString());				
		            
		        }

		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
