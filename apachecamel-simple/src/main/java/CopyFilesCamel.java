import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CopyFilesCamel {

	public static void main(String[] args) throws Exception {
		
		CamelContext context = new DefaultCamelContext();
		
		try {
			context.addRoutes(new RouteBuilder() {              // Add/create route by RouteBuilder.
				@Override
				public void configure() throws Exception {     // Configure provides two lines "From" and "to"
					from("file:data/input?noop=true")         // Use case is: Copy that'file:' from this location: data/input
					.to("file:data/output");                 // Send that on destination: "data/output"
				}
				
			});
		} catch (Exception e) {			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		context.start();     // Ones when the started context, thats when this route and instructs will execute. 
		Thread.sleep(5000);  // We have to let the Thread to sleep, so that the Camel will have inaf time to copy the files 'from' this folder 'to' this folder.   
		context.stop();    // When the file copy is done, "stop the context"
	}
}
