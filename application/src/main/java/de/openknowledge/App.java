/*
 * Copyright (C) open knowledge GmbH.
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

package de.openknowledge;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Persistence;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

//TODO Füge ok checkstyle und jacoco hinzu
//TODO configuration.xml ImportOrder bearbeiten

public class App {
    public static final int MILLIES_VALUE_ONE = 60;
    public static final int MILLIES_VALUE_TWO = 1000;


    public static void main(String[] args) throws Exception {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(LoginResource.class);

        factoryBean.setResourceProvider(
            new SingletonResourceProvider(
                new LoginResource(
                    new Repository(Persistence.createEntityManagerFactory("servletDB")
            .createEntityManager()))));
        Map<Object, Object> extensionMappings = new HashMap<Object, Object>();
        extensionMappings.put("json", MediaType.APPLICATION_JSON);
        factoryBean.setExtensionMappings(extensionMappings);

        List<Object> providers = new ArrayList<Object>();
        providers.add(new JAXBElementProvider());
        providers.add(new JacksonJsonProvider());
        factoryBean.setProviders(providers);

        factoryBean.setAddress("http://localhost:8080/");

        Server server = factoryBean.create();

        System.out.println("Server ready...");
        Thread.sleep(MILLIES_VALUE_ONE * MILLIES_VALUE_TWO);
        System.out.println("Server exiting");
        server.destroy();
        System.exit(0);
    }
}
