package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class representing a request in http protocol.
 * @author Vilim Staroveški
 *
 */
public class RequestContext {
	/**
	 * Class representing a cookie for request.
	 * @author Vilim Staroveški
	 *
	 */
	public static class RCCookie {
		/**
		 * Cookie name
		 */
		private String name; 
		/**
		 * Cookie value.
		 */
		private String value; 
		/**
		 * Cookie domain.
		 */
		private String domain;
		/**
		 * Cookie path.
		 */
		private String path;
		/**
		 * Cookie maximum age.
		 */
		private Integer maxAge;
		/**
		 * Type of cookie. Can be null.
		 */
		private String cookieType;
		/**
		 * Creates a new {@link RCCookie}
		 * @param name cookie name
 		 * @param value cookie value
		 * @param maxAge cookie maximum age
		 * @param domain cookie domain
		 * @param path cookie path
		 * @param cookieType cookie type
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain,
				String path, String cookieType) {
			
			this.value = value;
			this.name = name;
			this.maxAge = maxAge;
			this.domain = domain;
			this.path = path;
			this.cookieType = cookieType;
		}
		/**
		 * Returns cookie name.
		 * @return cookie name.
		 */
		public String getName() {
			return name;
		}
		/**
		 * Returns cookie value
		 * @return cookie value
		 */
		public String getValue() {
			return value;
		}
		/**
		 * Returns cookie domain
		 * @return cookie domain
		 */
		public String getDomain() {
			return domain;
		}
		/**
		 * Returns cookie path
		 * @return cookie path
		 */
		public String getPath() {
			return path;
		}
		/**
		 * Returns cookie maximum age
		 * @return cookie maximum age
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}
	/**
	 * Stream where this {@link RequestContext} will be sent to.
	 */
	private OutputStream outputStream;
	/**
	 * Charset used in request body.
	 */
	private Charset charset;
	/**
	 * Encoding used for request header. Write only parameter.
	 */
	private String encoding;//write only
	/**
	 * Status code for request. Write only parameter.
	 */
	private int statusCode;//write only
	/**
	 * Status text for request. Write only parameter.
	 */
	private String statusText;//write only
	/**
	 * Mime type for request. Write only parameter.
	 */
	private String mimeType;//write only
	/**
	 * Parameters of request.
	 */
	private Map<String, String> parameters;//read only
	/**
	 * Temporary parameters of request.
	 */
	private Map<String, String> temporaryParameters;
	/**
	 * Persistent parameters of request.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * Output cookies of request.
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Flag for header generated or not.
	 */
	private boolean headerGenerated;
	/**
	 * Creates new {@link RequestContext}
	 * @param outputStream Stream where this {@link RequestContext} will be sent to.
	 * @param parameters Parameters of request.
	 * @param persistentParameters Persistent parameters of request.
	 * @param outputCookies Output cookies of request.
	 */
	public RequestContext(
			OutputStream outputStream, // must not be null!
			Map<String,String> parameters, // if null, treat as empty
			Map<String,String> persistentParameters, // if null, treat as empty
			List<RCCookie> outputCookies) { // if null, treat as empty
		
		if(outputStream == null) {
			throw new NullPointerException();
		}
		this.outputStream = outputStream;
		this.encoding = "UTF-8";
		this.statusCode = 200;
		this.statusText = "OK";
		this.mimeType  = "text/html";
		this.parameters = 
				parameters == null ? 
						new HashMap<String, String>() : parameters;
		this.parameters = Collections.unmodifiableMap(this.parameters);
		this.persistentParameters = 
				persistentParameters == null ? 
						new HashMap<String, String>() : persistentParameters;
		this.outputCookies = 
				outputCookies == null ?	
						new ArrayList<RequestContext.RCCookie>() : outputCookies;
		this.headerGenerated = false;
		this.temporaryParameters = new HashMap<String, String>();
	}

	/**
	 * Sets encoding of this {@link RequestContext}.
	 * @param encoding new encoding.
	 */
	public void setEncoding(String encoding) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.encoding = encoding;
	}
	/**
	 * Sets status code of this {@link RequestContext}.
	 * @param statusCode status code.
	 */
	public void setStatusCode(int statusCode) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.statusCode = statusCode;
	}
	/**
	 * Sets status text of this {@link RequestContext}.
	 * @param statusText status text.
	 */
	public void setStatusText(String statusText) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.statusText = statusText;
	}
	/**
	 * Sets mime type of this {@link RequestContext}.
	 * @param mimeType mime type
	 */
	public void setMimeType(String mimeType) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.mimeType = mimeType;
	}
	/**
	 * Adds {@link RCCookie}. 
	 * @param rcCookie additional {@link RCCookie}
	 */
	public void addRCCookie(RCCookie rcCookie) {
		if(headerGenerated) {
			throw new RuntimeException();
		}
		this.outputCookies.add(rcCookie);
	}
	/**
	 * Returns parameter value defined by name 
	 * @param name name of parameter.
	 * @return value of parameter.
	 */
	public String getParameter(String name) {
		
		return this.parameters.get(name);
	}
	/**
	 * Returns unmodifiable set containing all of parameter names.
	 * @return unmodifiable set containing all of parameter names.
	 */
	public Set<String> getParameterNames() {
		
		return Collections.unmodifiableSet(
				this.parameters.keySet()
				);
	}
	/**
	 * Returns persistant parameter value defined by name
	 * @param name name of persistant parameter.
	 * @return value of parameter.
	 */
	public String getPersistentParameter(String name) {
		
		return this.persistentParameters.get(name);
	}
	/**
	 * Returns unmodifiable set containing all persistant parameter names.
	 * @return unmodifiable set containing all persistant parameter names.
	 */
	public Set<String> getPersistentParameterNames() {
		
		return Collections.unmodifiableSet(
				this.persistentParameters.keySet()
				);
	}
	/**
	 * Set persistant parameter value defined by parameter name.
	 * @param name name of parameter.
	 * @param value new value for parameter.
	 */
	public void setPersistentParameter(String name, String value) {
		
		this.persistentParameters.put(name, value);
	}
	/**
	 * Removes persistant paremeter defined by name
	 * @param name name of persistant parameter.
	 */
	public void removePersistentParameter(String name) {
		
		this.persistentParameters.remove(name);
	}
	/**
	 * Returns temporary parameter value defined by temporary parameter name.
	 * @param name name of temporary parameter
	 * @return value of temporary parameter
	 */
	public String getTemporaryParameter(String name) {
		
		return this.temporaryParameters.get(name);
	}
	/**
	 * Returns unmodifiable set containing all temporary parameter names.
	 * @return unmodifiable set containing all temporary parameter names.
	 */
	public Set<String> getTemporaryParameterNames() {
		
		return Collections.unmodifiableSet(
				this.temporaryParameters.keySet()
				);
	}
	/**
	 * Set temporary parameter value defined by temporary parameter name.
	 * @param name name of temporary parameter.
	 * @param value new value for temporary parameter.
	 */
	public void setTemporaryParameter(String name, String value) {
		
		this.temporaryParameters.put(name, value);
	}
	/**
	 * Removes temporary parameter defined by name.
	 * @param name name of temporary parameter.
	 */
	public void removeTemporaryParameter(String name) {
		
		this.temporaryParameters.remove(name);
	}
	/**
	 * Writes byte data to private output stream
	 * @param data bytes that is going to be written
	 * @return this {@link RequestContext}
	 * @throws IOException if some IO error occures while writing.
	 */
	public RequestContext write(byte[] data) throws IOException {
		
		if(!headerGenerated) {
			generateHeader();
		}
		this.outputStream.write(data);
		this.outputStream.flush();
		return this;
	}
	/**
	 * Method that closes output stream.
	 * @throws IOException if some IO error occures while closing.
	 */
	public void close() throws IOException {
		this.outputStream.close();
	}
	/**
	 * Writes {@link String} data to private output stream
	 * @param text {@link String} data that is going to be written
	 * @return this {@link RequestContext}
	 * @throws IOException if some IO error occures while writing.
	 */
	public RequestContext write(String text) throws IOException {
		
		if(!headerGenerated) {
			generateHeader();
		}
		this.outputStream.write(text.getBytes(this.encoding));
		this.outputStream.flush();
		return this;
	}
	/**
	 * Generates a header for this {@link RequestContext} and writes it to the output stream.
	 * @throws IOException if some IO error occures while writing.
	 */
	private void generateHeader() throws IOException {
		
		this.charset = Charset.forName(encoding);
		String appends = "";
		if(mimeType.startsWith("text/")) {
			appends = "; charset=" + this.encoding;
		}
		String cookies = "";
		for(RCCookie cookie : outputCookies) {
			cookies += "Set-Cookie: "+ 
					( ( cookie.name == null || cookie.value == null) ? "": cookie.name+"=\""+cookie.value+"\"" )+
					( cookie.domain == null ? "" : "; Domain="+cookie.domain )+ 
					( cookie.path == null ? "" : "; Path="+cookie.path )+ 
					( cookie.maxAge == null ? "" : "; Max-Age="+cookie.maxAge )+
					( cookie.cookieType == null ? "" : "; "+cookie.cookieType )+
					"\r\n";
		}
		
		byte[] header = ("HTTP/1.1 "+statusCode+" "+statusText+"\r\n" +
						"Content-Type: "+mimeType+appends+"\r\n"+
						cookies+
						"\r\n").getBytes(StandardCharsets.ISO_8859_1);
		
		headerGenerated = true;
		this.outputStream.write(header);
	}
}
