package us.lazytree.secrets;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.lang.System;
import java.util.Date;

@RestController
@RequestMapping("/")
public class Controller {

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "/env", produces = "application/json")
	public @ResponseBody String getEnv(@PathVariable String env_var)
		throws Exception {
		String envVar = System.getenv(env_var);
		return String.format("Variable given: %s  Variable Value: %s ", env_var, envVar);
	}
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "/stores/{VarName}/{VarPass}", produces = "text/plain")
	// https://docs.spring.io/spring/docs/3.1.x/spring-framework-reference/html/mvc.html#mvc-ann-requestmapping-uri-templates
	public String getKeystore(@PathVariable String VarName, String VarPass)
		throws Exception {
	// DRY?! Can I get /env... from getEnv it is public
		String jksPath = System.getenv(VarName);
		String jksPass = System.getenv(VarPass);
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(new FileInputStream(jksPath), jksPass.toCharArray());
		// Add in future, ability to search/return alias
		Enumeration aliases = keystore.aliases(); // Does this need a type defination?
		String alias = "";
		while (aliases.hasMoreElements()) { //Or should I use .next/hasNext()
			alias = (String) aliases.nextElement();
			try {
				Date CertExpireDate = ((X509Certificate) keystore.getCertificate(alias)).getNotAfter();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		return alias;
	}
	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/stores", produces = "text/plain")
	public @ResponseBody String postStore(@PathVariable String env_var) {
		String PostedData = "";
		return PostedData;
	}

}
