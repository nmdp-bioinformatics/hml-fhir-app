package org.nmdp.fhirsubmission.http;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 8/16/17.
 * <p>
 * fhir-submission
 * Copyright (c) 2012-2017 National Marrow Donor Program (NMDP)
 * <p>
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 * <p>
 * > http://www.fsf.org/licensing/licenses/lgpl.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Post {

    private static final Logger LOG = Logger.getLogger(Post.class);
    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String BACKSLASH = "\\\\";
    private static final String OPENING_BRACKET = "\\{";
    private static final String RESOURCE_KEY = "resource";
    private static final Gson GSON = new GsonBuilder().create();

    private final HttpClient CLIENT;

    public Post() {
        this.CLIENT = HttpClientBuilder.create().build();
    }

    public <T> HttpResponse syncPost(T data, String url, JsonSerializer serializer, Class<T> clazz) {
        return sendPost(data, url, serializer, clazz, this.CLIENT);
    }

    public static <T> HttpResponse post(T data, String url, JsonSerializer serializer, Class<T> clazz) {
        HttpClient client = HttpClientBuilder.create().build();
        return sendPost(data, url, serializer, clazz, client);
    }

    public static List<HttpResponse> postBatch(String url, JsonArray batch) {
        return sendBatchPost(url, batch);
    }

    private static List<HttpResponse> sendBatchPost(String url, JsonArray bundles) {
        HttpClient client = HttpClientBuilder.create().build();
        List<HttpResponse> responses = new ArrayList<>();

        try {
            Iterator iterator = bundles.iterator();

            while (iterator.hasNext()) {
                JsonObject json = (JsonObject) iterator.next();
                String jsonString = GSON.toJson(json);
                String jsonFormattedString = jsonString
                        .replaceAll(String.format("%s%s%s%s", DOUBLE_QUOTE, OPENING_BRACKET, BACKSLASH, DOUBLE_QUOTE),
                                String.format("%s%s", OPENING_BRACKET, DOUBLE_QUOTE));
                jsonFormattedString = jsonFormattedString
                        .replaceAll(String.format("}%s", DOUBLE_QUOTE), "}");
                jsonFormattedString = jsonFormattedString
                        .replaceAll(String.format("%s%s", BACKSLASH, DOUBLE_QUOTE), DOUBLE_QUOTE);

                responses.add(sendPost(jsonFormattedString, url, client));
            }
        } catch (UnsupportedEncodingException ex) {
            LOG.error(ex);
        } catch (IOException ex) {
            LOG.error(ex);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        } finally {
            return responses;
        }
    }

    private static HttpResponse sendPost(String data, String url, HttpClient client) throws UnsupportedEncodingException, IOException {
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(data);
        post.setEntity(entity);
        post.setHeader(HEADER_KEY, HEADER_VALUE);

        return client.execute(post);
    }

    private static <T> HttpResponse sendPost(T data, String url, JsonSerializer serializer, Class<T> clazz, HttpClient client) {
        HttpPost post = new HttpPost(url);
        HttpResponse response = null;

        try {
            String json;

            if (serializer == null) {
                json = GSON.toJson(data);
            } else {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(clazz, serializer);
                Gson gson = gsonBuilder.create();
                json = gson.toJson(data);
            }

            return sendPost(json, url, client);
        } catch (UnsupportedEncodingException ex) {
            LOG.error(ex);
        } catch (IOException ex) {
            LOG.error(ex);
        } finally {
            return response;
        }
    }
}
