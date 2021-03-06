package com.eclipsesource.jaxrs.publisher.internal;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;


public class Configuration_Test {
  
  private JAXRSConnector connector;
  private Configuration config;

  @Before
  public void setUp() {
    connector = mock( JAXRSConnector.class );
    config = new Configuration( connector );
  }
  
  @Test
  public void testUpdateWithNull() throws Exception {
    config.updated( null );
    
    verify( connector, never() ).updateConfiguration( anyString(), eq(false));
  }
  
  @Test
  public void testUpdateWithPath() throws Exception {
    config.updated( createProperties( "/test" ) );
    
    verify( connector ).updateConfiguration( "/test" , false);
  }
  
  @Test
  public void testUpdateWithPath2() throws Exception {
    config.updated( createProperties( "/test2" ) );
    
    verify( connector ).updateConfiguration( "/test2" , false );
  }
  
  @Test
  public void testUpdateWithDisabledWadl() throws Exception {
    config.updated( createProperties( "/test", true ) );
    
    verify( connector ).updateConfiguration( "/test" , true );
  }
  
  @Test( expected = ConfigurationException.class )
  public void testUpdateWithInvalidPath() throws Exception {
    config.updated( createProperties( "test" ) );
  }
  
  @Test( expected = ConfigurationException.class )
  public void testUpdateWithEmptyProperties() throws Exception {
    config.updated( new Hashtable<String, Object>() );
  }

  private Dictionary<String, ?> createProperties( String path ) {
    return createProperties( path, false);
  }
  
  private Dictionary<String, ?> createProperties( String path, Boolean disableWadl) {
    Hashtable<String, Object> properties = new Hashtable<String, Object>();
    properties.put( Configuration.ROOT_PROPERTY, path );
    properties.put( Configuration.WADL_DISABLE_PROPERTY, disableWadl );
    return properties;
  }

}
