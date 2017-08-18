package cn.allchin.httpPostRpc.serialize.support;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.apache.http.client.methods.HttpPost;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import cn.allchin.httpPostRpc.serialize.ProtocolUtil;  
/**
 * xml 序列化工具
 * @author renxing.zhang
 *
 */
public class JaxbXmlUtil implements ProtocolUtil {
	/* (non-Javadoc)
	 * @see com.qunar.flight.nts.checkin.util.XmlUtil#serialise(java.lang.Object)
	 * 
	 * 
	 */
	public   String serialise(Object obj,HttpPost request) { 
		request.setHeader("Content-Type", "text/xml; charset=utf-8");
		String result = null;  
        StringWriter writer = new StringWriter();  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller();  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");  
            //去掉ns2前缀
            NamespacePrefixMapper mapper = new PreferredMapper();  
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);  
            
            // marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);  
           /* marshaller.setProperty("com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler",  
                            new CharacterEscapeHandler() {  
                                public void escape(char[] chars, int start,  
                                        int length, boolean isAttVal,  
                                        Writer writer) throws IOException {  
                                    writer.write(chars, start, length);  
                                }  
                            });  */
  
              
            marshaller.marshal(obj, writer);  
            // result = writer.toString();  
            // remove the standalone="yes" in the xml header  
            
            result = writer.toString().replace("standalone=\"yes\"", "");  //  smell bad
            result=result.replaceAll("ns2:", "");//  会出现ns2前缀
            result=result.replaceAll(":ns2", ""); 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }finally{  
            if(writer!=null){  
                    try {  
                        writer.close();  
                    } catch (IOException e) { }  
            }  
        }  
  
        return result;  
	}
	/* (non-Javadoc)
	 * @see com.qunar.flight.nts.checkin.util.XmlUtil#deserialize(java.lang.String, java.lang.Class)
	 * 
	 * 
	 * 
	 */
	public   <T> T deserialize(String xml, Class<T> clazz) {
		if(!xml.startsWith("<")){//  fix前言中不允许有内容: Smell bad
			xml=xml.substring(xml.indexOf("<"));
		}
		T _clazz = null;  
        StringReader reader = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(clazz);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            reader = new StringReader(xml);  
            SAXParserFactory sax = SAXParserFactory.newInstance();  
            sax.setNamespaceAware(false);  
            XMLReader xmlReader = sax.newSAXParser().getXMLReader();  
            Source source = new SAXSource(xmlReader, new InputSource(reader));  
            
            
            _clazz = (T) unmarshaller.unmarshal(source);  
        } catch (Exception e) {
        	Character c=xml.charAt(0); 
            throw new RuntimeException(e);  
        }finally{  
            if(reader!=null){  
                reader.close();  
            }  
        }  
  
        return _clazz;  
	}
	
    public static class PreferredMapper extends NamespacePrefixMapper {  
        @Override  
        public String getPreferredPrefix(String namespaceUri,  
                String suggestion, boolean requirePrefix) {  
            return "";  
        }   
          
    }
 
    
}
