package runSalvarArquivos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import domain.ArquivosContrato;
import domain.ArquivosContrato2;

public class  runSalvar3 {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();
	
	public static void main(String[] args) {
		
		try{
							
	    	Query q = em.createQuery("select a from ArquivosContrato a where a.id = 1599 order by a.id", ArquivosContrato.class);
	    	//q.setFirstResult(1600);
	    	//q.setMaxResults(100);
	    		    	
	    	List<ArquivosContrato> result = q.getResultList();    	  
	    	
    		em.getTransaction().begin();
	    	for (ArquivosContrato arquivo: result) {	    		
	    		writeFile((byte[]) arquivo.getFile(), arquivo.getNome(),arquivo.getContrato().toString(), arquivo.getContrato(), arquivo.getData(), arquivo.getUsuario());
			}
	    	em.getTransaction().commit();
    	
		}catch(Exception e){
			e.printStackTrace();
		}
    	
	}
	
	public static void writeFile(byte[] data, String nome, String contrato, Integer codContrato, Date date, String usuario) throws IOException
	 {
		   File file = new File("E:\\ARQUIVOS\\CONTRATOS\\"+nome);
		
		   OutputStream fo = new FileOutputStream(file);
		   fo.write(data);
		   fo.flush();
		   fo.close();		   
		   
		   String link = fazerUpload(file);
 			     			    
 			ArquivosContrato2 arquivo2 = new ArquivosContrato2(null, codContrato, nome, date);
	    	arquivo2.setUsuario(usuario);
	    	arquivo2.setLink(link);
	    	
	    	em.persist(arquivo2); 		 
	 }
	private static String fazerUpload(File file){
		try{
			OkHttpClient client = new OkHttpClient().newBuilder().build();
		   MediaType mediaType = MediaType.parse("text/plain");
		   RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("File",file.getAbsolutePath(),
		   RequestBody.create(MediaType.parse("application/octet-stream"),file))
			      .addFormDataPart("tag",file.getName())
			      .addFormDataPart("bucket_name","opuserp")
			      .build();
			    Request request = new Request.Builder()
			      .url("http://172.17.0.90:8080/uploadFile")
			      .method("POST", body)
			      .build();
			    Response response = client.newCall(request).execute();
			    return response.body().string();
		}catch(Exception e){
			e.printStackTrace();			
			return null;
		}
	}
	
}
