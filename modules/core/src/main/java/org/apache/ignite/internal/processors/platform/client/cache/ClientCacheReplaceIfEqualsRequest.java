/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.platform.client.cache;

import org.apache.ignite.internal.IgniteInternalFuture;
import org.apache.ignite.internal.binary.BinaryRawReaderEx;
import org.apache.ignite.internal.processors.platform.client.ClientBooleanResponse;
import org.apache.ignite.internal.processors.platform.client.ClientConnectionContext;
import org.apache.ignite.internal.processors.platform.client.ClientResponse;

/**
 * Cache replace request.
 */
public class ClientCacheReplaceIfEqualsRequest extends ClientCacheKeyValueRequest {
    /** New value. */
    private final Object newVal;

    /**
     * Ctor.
     *
     * @param reader Reader.
     */
    public ClientCacheReplaceIfEqualsRequest(BinaryRawReaderEx reader) {
        super(reader);

        newVal = reader.readObjectDetached();
    }

    /** {@inheritDoc} */
    @Override public ClientResponse process0(ClientConnectionContext ctx) {
        boolean res = cache(ctx).replace(key(), val(), newVal);

        return new ClientBooleanResponse(requestId(), res);
    }

    /** {@inheritDoc} */
    @Override public IgniteInternalFuture<ClientResponse> processAsync0(ClientConnectionContext ctx) {
        return chainFuture(cache(ctx).replaceAsync(key(), val(), newVal), v -> new ClientBooleanResponse(requestId(), v));
    }
}
