package com.hb.schedular.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

@Configuration
public class ConfigLoaderUtil {

  private static Logger logger = LoggerFactory.getLogger(ConfigLoaderUtil.class);

  public File getTaskConfigs() throws FileNotFoundException {
    return ResourceUtils.getFile("classpath:tasks-config.json");
  }
}
