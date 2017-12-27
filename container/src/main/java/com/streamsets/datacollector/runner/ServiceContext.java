/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.datacollector.runner;

import com.codahale.metrics.MetricRegistry;
import com.streamsets.datacollector.config.ConfigDefinition;
import com.streamsets.datacollector.util.ElUtil;
import com.streamsets.pipeline.api.service.Service;

import java.util.HashMap;
import java.util.Map;

public class ServiceContext extends ProtoContext implements Service.Context {

  public ServiceContext(
      Map<String, Object> constants,
      MetricRegistry metrics,
      String pipelineId,
      String rev,
      String stageName,
      ServiceRuntime serviceRuntime,
      String serviceName,
      String resourceDir
  ) {
    super(
      getConfigToElDefMap(serviceRuntime.getServiceBean().getDefinition().getConfigDefinitions()),
      constants,
      metrics,
      pipelineId,
      rev,
      stageName,
      serviceName,
      resourceDir
    );
  }

}
