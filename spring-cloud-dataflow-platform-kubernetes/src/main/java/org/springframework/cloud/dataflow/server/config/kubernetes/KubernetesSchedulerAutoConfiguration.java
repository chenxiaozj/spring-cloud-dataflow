/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.server.config.kubernetes;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.dataflow.server.config.features.SchedulerConfiguration;
import org.springframework.cloud.scheduler.spi.core.Scheduler;
import org.springframework.cloud.scheduler.spi.kubernetes.KubernetesScheduler;
import org.springframework.cloud.scheduler.spi.kubernetes.KubernetesSchedulerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the Spring Cloud Kubernetes Scheduler based on feature toggle settings and if running on
 * Kubernetes.
 *
 * @author Chris Schaefer
 */
@Configuration
@Conditional({ SchedulerConfiguration.SchedulerConfigurationPropertyChecker.class })
@ConditionalOnProperty(name = "kubernetes.service.host")
public class KubernetesSchedulerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Scheduler scheduler(KubernetesSchedulerProperties kubernetesSchedulerProperties) {
		KubernetesClient kubernetesClient = new DefaultKubernetesClient()
				.inNamespace(kubernetesSchedulerProperties.getNamespace());

		return new KubernetesScheduler(kubernetesClient, kubernetesSchedulerProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	public KubernetesSchedulerProperties kubernetesSchedulerProperties() {
		return new KubernetesSchedulerProperties();
	}
}
