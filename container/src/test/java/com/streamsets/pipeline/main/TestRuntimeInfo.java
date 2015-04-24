/**
 * (c) 2014 StreamSets, Inc. All rights reserved. May not
 * be copied, modified, or distributed in whole or part without
 * written consent of StreamSets, Inc.
 */
package com.streamsets.pipeline.main;

import com.codahale.metrics.MetricRegistry;
import com.streamsets.pipeline.util.Configuration;
import dagger.ObjectGraph;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class TestRuntimeInfo {

  @Before
  @After
  public void cleanUp() {
    System.getProperties().remove(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.CONFIG_DIR);
    System.getProperties().remove(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.LOG_DIR);
    System.getProperties().remove(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.DATA_DIR);
    System.getProperties().remove(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.STATIC_WEB_DIR);
    System.getProperties().remove(RuntimeModule.DATA_COLLECTOR_BASE_HTTP_URL);
    System.getProperties().remove(RuntimeModule.DATA_COLLECTOR_ID);
  }

  @Test
  public void testInfoDefault() {
    RuntimeInfo info = new RuntimeInfo(RuntimeModule.SDC_PROPERTY_PREFIX, new MetricRegistry(),
      Arrays.asList(getClass().getClassLoader()));
    Assert.assertEquals(System.getProperty("user.dir"), info.getRuntimeDir());
    Assert.assertEquals(System.getProperty("user.dir") + "/sdc-static-web", info.getStaticWebDir());
    Assert.assertEquals(System.getProperty("user.dir") + "/etc", info.getConfigDir());
    Assert.assertEquals(System.getProperty("user.dir") + "/log", info.getLogDir());
    Assert.assertEquals(System.getProperty("user.dir") + "/var", info.getDataDir());
    Assert.assertEquals(Arrays.asList(getClass().getClassLoader()), info.getStageLibraryClassLoaders());
    Logger log = Mockito.mock(Logger.class);
    info.log(log);
  }

  @Test
  public void testInfoCustom() {
    System.setProperty(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.STATIC_WEB_DIR, "w");
    System.setProperty(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.CONFIG_DIR, "x");
    System.setProperty(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.LOG_DIR, "y");
    System.setProperty(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.DATA_DIR, "z");

    List<? extends ClassLoader> customCLs = Arrays.asList(new URLClassLoader(new URL[0], null));
    RuntimeInfo info = new RuntimeInfo(RuntimeModule.SDC_PROPERTY_PREFIX, new MetricRegistry(), customCLs);
    Assert.assertEquals(System.getProperty("user.dir"), info.getRuntimeDir());
    Assert.assertEquals("w", info.getStaticWebDir());
    Assert.assertEquals("x", info.getConfigDir());
    Assert.assertEquals("y", info.getLogDir());
    Assert.assertEquals("z", info.getDataDir());
    Assert.assertEquals(customCLs, info.getStageLibraryClassLoaders());
    Logger log = Mockito.mock(Logger.class);
    info.log(log);
  }

  @Test
  public void testAttributes() {
    RuntimeInfo info = new RuntimeInfo(RuntimeModule.SDC_PROPERTY_PREFIX, new MetricRegistry(),
      Arrays.asList(getClass().getClassLoader()));
    Assert.assertFalse(info.hasAttribute("a"));
    info.setAttribute("a", 1);
    Assert.assertTrue(info.hasAttribute("a"));
    Assert.assertEquals(1, info.getAttribute("a"));
    info.removeAttribute("a");
    Assert.assertFalse(info.hasAttribute("a"));
  }

  @Test
  public void testDefaultIdAndBaseHttpUrl() {
    ObjectGraph og  = ObjectGraph.create(RuntimeModule.class);
    RuntimeInfo info = og.get(RuntimeInfo.class);
    Assert.assertEquals("UNDEF", info.getId());
    Assert.assertEquals("UNDEF", info.getBaseHttpUrl());
  }

  @Test
  public void testMetrics() {
    ObjectGraph og  = ObjectGraph.create(RuntimeModule.class);
    RuntimeInfo info = og.get(RuntimeInfo.class);
    Assert.assertNotNull(info.getMetrics());
  }

  @Test
  public void testConfigIdAndBaseHttpUrl() throws Exception {
    File dir = new File("target", UUID.randomUUID().toString());
    Assert.assertTrue(dir.mkdirs());
    System.setProperty(RuntimeModule.SDC_PROPERTY_PREFIX + RuntimeInfo.CONFIG_DIR, dir.getAbsolutePath());
    Properties props = new Properties();
    props.setProperty(RuntimeModule.DATA_COLLECTOR_BASE_HTTP_URL, "HTTP");
    props.setProperty(RuntimeModule.DATA_COLLECTOR_ID, "ID");
    Writer writer = new FileWriter(new File(dir, "sdc.properties"));
    props.store(writer, "");
    writer.close();
    ObjectGraph og  = ObjectGraph.create(RuntimeModule.class);
    og.get(Configuration.class);
    RuntimeInfo info = og.get(RuntimeInfo.class);
    Assert.assertEquals("ID", info.getId());
    Assert.assertEquals("HTTP", info.getBaseHttpUrl());
  }

}
