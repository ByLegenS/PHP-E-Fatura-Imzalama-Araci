package ntg.bls;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ntg.bls.SampleBase;
import ntg.bls.SmartCardManager;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transform;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.ClaimedRole;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignerRole;

public class anadosya extends SampleBase {
	static String degistir;
	static String degistirSonuc;
	static List<String> sonuc;
	static String GelenDosya;
	static String PIN;
	static Boolean SeriImza;
	static Boolean DosyaCikti;
	static Node eklenecekalan;
	
	public static void main(String[] GelenVeri) {
		try {
		
			/*if(GelenVeri.length == 0)
			    degistirSonuc = "TicariFaturaOrnegi.xml,123456,false,false";
			else {*/
				degistir = GelenVeri[0].replace("{","");
			    degistirSonuc = degistir.replace("}","");
			/*}*/
			
		    sonuc = new ArrayList<String>(Arrays.asList(degistirSonuc.split(",")));
		    
		    /*if(GelenVeri.length == 0) GelenDosya = "imzalandi.xml";
		    else*/ GelenDosya = sonuc.get(0);
		    
		    PIN = sonuc.get(1);
		    SeriImza = Boolean.parseBoolean(sonuc.get(2));
		    DosyaCikti = Boolean.parseBoolean(sonuc.get(3));
			
		    /*
			System.out.println("GelenDosya : "+GelenDosya);
			System.out.println("PIN : "+PIN);
			System.out.println("SeriImza : "+SeriImza);
			System.out.println("DosyaCikti : "+DosyaCikti);
			*/
		    
			Context context = createContext();
	        org.w3c.dom.Document dosya = parseDoc(GelenDosya, context);
			
	        // create signature according to context,
	        // with default type (XADES_BES)
	        XMLSignature signature = new XMLSignature(context, false);
	
	        /* Seri İmza */
	        if(SeriImza == true)
	        	eklenecekalan = dosya.getDocumentElement().getElementsByTagName("oa:Signature").item(0);
	        else
	        	eklenecekalan = dosya.getDocumentElement().getElementsByTagName("ext:ExtensionContent").item(0);
	        
	        eklenecekalan.setTextContent("");
	        eklenecekalan.appendChild(signature.getElement());
	
	        // false-true gets non-qualified certificates while true-false gets qualified ones
	        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(IS_QUALIFIED, !IS_QUALIFIED);
	
	        // add certificate to show who signed the document
	        signature.addKeyInfo(cert);
	
			// use enveloped signature transform
			Transforms transforms = new Transforms(context);
			transforms.addTransform(new Transform(context, TransformType.ENVELOPED.getUrl()));
			signature.addDocument("", null, transforms, DigestMethod.SHA_256, null, false);
	
			//signature.addKeyInfo(new ECertificate(cert.getEncoded()));
	
	        //add signer role information
	        SignerRole rol = new SignerRole(context, new ClaimedRole[]{new ClaimedRole(context, "Tedarikçi")});
	        signature.getQualifyingProperties().getSignedSignatureProperties().setSignerRole(rol);
	
	        signature.setSigningTime(Calendar.getInstance());
			//e-fatura standards want signatureID to be same with cbc:URI
			// get signatureID from e-fatura
			String signatureID = ((Element)(dosya.getDocumentElement().getElementsByTagName("cbc:URI").item(0)) ).getTextContent();
			String signatureIDwoNumberSign = signatureID.substring(1);
			Element dsSignature = (Element)(dosya.getDocumentElement().getElementsByTagName("ds:Signature").item(0));
			dsSignature.setAttribute("Id", signatureIDwoNumberSign);
			dsSignature.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
			Element xadesQualifyingProperties = (Element)(dosya.getDocumentElement().getElementsByTagName("xades:QualifyingProperties").item(0));
			xadesQualifyingProperties.setAttribute("Target", "#"+signatureIDwoNumberSign);
			xadesQualifyingProperties.setAttribute("xmlns:xades", "http://uri.etsi.org/01903/v1.3.2#");
			
	        // add signing time
			signature.getSignedInfo().setCanonicalizationMethod(C14nMethod.INCLUSIVE_WITH_COMMENTS);        
			signature.getSignedInfo().setSignatureMethod(SignatureMethod.RSA_SHA256);
	
	        
	        // now sign it by using smart card
	        signature.sign(SmartCardManager.getInstance().getSigner(PIN, cert));
	
	        /* Seri İmza */
	        if(SeriImza == true)
	        {
				XMLSignature counterSignature = signature.createCounterSignature();
				counterSignature.getSignedInfo().setSignatureMethod(SignatureMethod.RSA_SHA256);
				counterSignature.addKeyInfo(new ECertificate(cert.getEncoded()));  
				counterSignature.sign(SmartCardManager.getInstance().getSigner(PIN, cert));
	        }
	        
			/* Dosyaya Yaz */
			if(DosyaCikti == true)
			{
		        Source source = new DOMSource(dosya);
		        Transformer transformer = TransformerFactory.newInstance().newTransformer();
		        transformer.transform(source, new StreamResult(new FileOutputStream(GelenDosya)));	
		        System.out.println("İmzalandı...");
			}
			else
			{
				DOMSource domSource = new DOMSource(dosya);
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(domSource, result);
				System.out.println(writer.toString());
			}
			
	        /* Karttan çıkış yapılıyor */
			SmartCardManager.getInstance().logout();
	        
		} catch (Exception e) {
			System.out.println("------------------------------");
			System.out.println("Hata Oluştu !");
			e.printStackTrace();
			System.out.println("------------------------------");
			System.out.println("PHP Kullanımı:");
			System.out.println("exec(\"java -Dfile.encoding=UTF8 -jar java_jar_dosyasi {imzalanacak_xml,smart_card_pin,seri_imza_true_veya_false,dosyaya_kaydet_true_veya_false} 2>&1\", javadan_donen_deger);");
			System.out.println("exec(\"java -Dfile.encoding=UTF8 -jar C:\\BulunduguDizin\\NTGEFatura.jar {C:\\BulunduguDizin\\OrnekFatura.xml,123456,false,true} 2>&1\", $cikti);");
			System.out.println("------------------------------");
		}
	}

}
