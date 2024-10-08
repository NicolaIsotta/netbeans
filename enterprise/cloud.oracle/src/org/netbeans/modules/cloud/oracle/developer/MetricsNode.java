/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.cloud.oracle.developer;

import com.oracle.bmc.monitoring.MonitoringClient;
import com.oracle.bmc.monitoring.model.ListMetricsDetails;
import com.oracle.bmc.monitoring.requests.ListMetricsRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.netbeans.modules.cloud.oracle.ChildrenProvider;
import org.netbeans.modules.cloud.oracle.NodeProvider;
import org.netbeans.modules.cloud.oracle.OCINode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;

/**
 *
 * @author Dusan Petrovic
 */
@NbBundle.Messages({
    "MetricsDesc=Namespace: {0}"
})
public class MetricsNode extends OCINode {
    
    private static final String METRICS_ICON = "org/netbeans/modules/cloud/oracle/resources/metrics.svg"; // NOI18N

    public MetricsNode(MetricsItem instance) {
        super(instance, Children.LEAF);
        setName(instance.getName());
        setDisplayName(instance.getName());
        setIconBaseWithExtension(METRICS_ICON);
        setShortDescription(Bundle.MetricsDesc(instance.getNamespace()));
    }
    
    public static NodeProvider<MetricsItem> createNode() {
        return MetricsNode::new;
    }

    /**
     * Retrieves list of Metrics belonging to a given Compartment.
     *
     * @return Returns {@code ChildrenProvider} which fetches List of
     * {@code MetricsItem} for given {@code MetricsNamespaceItem}
     */
    public static ChildrenProvider.SessionAware<MetricsNamespaceItem, MetricsItem> getMetrics() {
        return (metricsNamespace, session) -> {
            MonitoringClient client = session.newClient(MonitoringClient.class);
            ListMetricsDetails listMetricsDetails = ListMetricsDetails.builder()
                    .namespace(metricsNamespace.getName())
                    .build();
            
            ListMetricsRequest request = ListMetricsRequest.builder()
                    .compartmentId(metricsNamespace.getCompartmentId())
                    .listMetricsDetails(listMetricsDetails)
                    .build();

            Set<String> uniqueMetrics = new HashSet<>();

            return client.listMetrics(request)
                    .getItems()
                    .stream()
                    .filter(m -> uniqueMetrics.add(m.getName()))
                    .map(d -> new MetricsItem(
                            d.getCompartmentId(),
                            d.getName(),
                            d.getNamespace(),
                            session.getRegion().getRegionId()
                    ))
                    .collect(Collectors.toList());
        };
    }
}
