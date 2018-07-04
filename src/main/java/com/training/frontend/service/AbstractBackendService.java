package com.training.frontend.service;

import com.google.gson.*;
import com.training.frontend.security.GrantedAuthorityTypeAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

@Configuration
public abstract class AbstractBackendService {

    protected static Logger LOGGER = LoggerFactory.getLogger(AbstractBackendService.class);
    protected String webServicesString = "";
    protected String resultString = "";

    protected RestTemplate restTemplate;

    protected JsonParser parser = new JsonParser();

    JsonSerializer<Date> ser = new JsonSerializer<Date>() {
        public JsonElement serialize(Date src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    protected Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").registerTypeAdapter(Date.class, deser).registerTypeAdapter(GrantedAuthority.class, new GrantedAuthorityTypeAdaptor()).create();

    public abstract void setRestTemplate(RestTemplate restTemplate);

    @Value("${EngineServer}")
    protected String EngineServer;

    public AbstractBackendService() {
    }

    public String getWebServicesString() {
        return webServicesString;
    }

    public AbstractBackendService setWebServicesString(String webServicesString) {
        this.webServicesString = webServicesString;
        return this;
    }

    public String getResultString() {
        LOGGER.debug("request :{}", getWebServicesString());
        resultString = restTemplate.getForObject(getWebServicesString(), String.class);
        return resultString;
    }

    public String getResultString(String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("request :{}", url);
        resultString = restTemplate.getForObject(url, String.class);
        return resultString;
    }

    public String getResultString(String urlParam, String param) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("request :{}", url);

        return restTemplate.getForObject(url, String.class, param);
    }

    public ResponseEntity<String> getResultString(String urlParam, HttpEntity<String> entity) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("request :{}", url);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> getResultByExchange(String urlParam) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Content-Type", "application/json; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        String url = this.EngineServer + urlParam;
        LOGGER.info("request :{}", url);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> getResultStringByTypeHttpMethodAndBodyContent(String json, HttpMethod httpMethod, String url, RestTemplate restTemplate) {
        LOGGER.debug("url :{}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Content-Type", "application/json; charset=utf-8");


        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
        if (httpMethod == null) {
            httpMethod = HttpMethod.GET;
        }
        ResponseEntity<String> reponseEntity = restTemplate.exchange(url, httpMethod, entity, String.class);
        return reponseEntity;
    }

    public ResponseEntity<String> getResultStringByTypeHttpMethodAndBodyContent(HttpMethod httpMethod, String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("url :{}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Content-Type", "application/json; charset=utf-8");


        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        if (httpMethod == null) {
            httpMethod = HttpMethod.GET;
        }
        ResponseEntity<String> reponseEntity = restTemplate.exchange(url, httpMethod, entity, String.class);
        return reponseEntity;
    }

    public ResponseEntity<String> postWithMapParameter(Map<String, String[]> parameterMap, HttpMethod httpMethod, String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("url :{}", url);

        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                if (!"parentId".equals(key) && !"_csrf".equals(key)) {
                    body.add(key, parameterMap.get(key)[0]);

                }


            }
        }


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(body, headers);
        if (httpMethod == null) {
            httpMethod = HttpMethod.POST;
        }

        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(mediaType));

        restTemplate.getMessageConverters().add(converter);
        ResponseEntity<String> reponseEntity = restTemplate.postForEntity(url, entity, String.class);
        return reponseEntity;
    }

    public ResponseEntity<String> postWithJson(Map<String, String[]> parameterMap, HttpMethod httpMethod, String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("url :{}", url);

        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        Map<String, String> body = new HashMap<String, String>();
        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                if (!"parentId".equals(key) && !"_csrf".equals(key)) {
                    body.put(key, parameterMap.get(key)[0]);

                }


            }
        }


        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(body), headers);
        LOGGER.info("><><>< url : {} ", url);
        LOGGER.info("><><>< entity : {} ", entity);

        //LOGGER.info("entity :{}", entity);
        if (httpMethod == null) {
            httpMethod = HttpMethod.POST;
        }

        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(mediaType));

        restTemplate.getMessageConverters().add(converter);
        ResponseEntity<String> reponseEntity = restTemplate.postForEntity(url, entity, String.class);

        return reponseEntity;
    }

    public ResponseEntity<String> postWithJsonRelation(Map<String, String[]> parameterMap, List<String> relationList, HttpMethod httpMethod, String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("url :{}", url);

        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        Map body = new HashMap<String, String>();
        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                if (!"parentId".equals(key) && !"_csrf".equals(key)) {
                    if (relationList.contains(key)) {
                        String jsonData = parameterMap.get(key)[0];
                        Map map = gson.fromJson(jsonData, Map.class);
                        body.put(key, map);
                    } else {
                        body.put(key, parameterMap.get(key)[0]);
                    }
                }
            }
        }

        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(body), headers);
        LOGGER.info("><><>< url : {} ", url);
        LOGGER.info("><><>< entity : {} ", entity);

        //LOGGER.info("entity :{}", entity);
        if (httpMethod == null) {
            httpMethod = HttpMethod.POST;
        }

        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(mediaType));

        restTemplate.getMessageConverters().add(converter);
        ResponseEntity<String> reponseEntity = restTemplate.postForEntity(url, entity, String.class);

        return reponseEntity;
    }


    public ResponseEntity<String> putWithJson(Map<String, String[]> parameterMap, HttpMethod httpMethod, String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("url :{}", url);

        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        Map<String, String> body = new HashMap<String, String>();
        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                body.put(key, parameterMap.get(key)[0]);
            }
        }


        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(body), headers);

        //LOGGER.info("entity :{}", entity);
        if (httpMethod == null) {
            httpMethod = HttpMethod.PUT;
        }

        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(mediaType));

        restTemplate.getMessageConverters().add(converter);
        //restTemplate.put(url, entity, String.class);
        ResponseEntity<String> reponseEntity = restTemplate.exchange(url, httpMethod, entity, String.class);

        return new ResponseEntity(reponseEntity.getBody(), HttpStatus.OK);
    }

    public ResponseEntity<String> putAssociate(List<String> associateIdLs, String urlParam, String associatePath) {
        String url = this.EngineServer + urlParam + associatePath;
        LOGGER.info("url :{}", url);

        MediaType mediaType = new MediaType("text", "uri-list", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        String content = "";
        if (associateIdLs != null) for (String id : associateIdLs) {
            content += this.EngineServer + associatePath + "/" + id + "\n";
        }


        HttpEntity<String> entity = new HttpEntity<String>(content, headers);


        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(mediaType));

        restTemplate.getMessageConverters().add(converter);
        //LOGGER.info("entity :{}", restTemplate);
        restTemplate.put(url, entity, String.class);

        return new ResponseEntity("", HttpStatus.OK);
    }


    public ResponseEntity<String> putParent(List<String> associateIdLs, String urlParam, String associatePath) {
        String url = this.EngineServer + urlParam + associatePath;
        LOGGER.info("putParent url :{}", url);

        MediaType mediaType = new MediaType("text", "uri-list", Charset.forName("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        String content = "";
        if (associateIdLs != null) for (String id : associateIdLs) {
            content += this.EngineServer + associatePath + "/" + id;
        }


        HttpEntity<String> entity = new HttpEntity<String>(content, headers);


        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(mediaType));

        restTemplate.getMessageConverters().add(converter);
        //LOGGER.info("entity :{}", restTemplate);
        restTemplate.put(url, entity, String.class);

        return new ResponseEntity("", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteSend(HttpMethod httpMethod, String urlParam) {
        String url = this.EngineServer + urlParam;
        LOGGER.info("url :{}", url);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        restTemplate.delete(url);
        return new ResponseEntity("", HttpStatus.OK);
    }
}
