package ru.smyslokod.gradle.plugin.model;

import java.util.List;
import java.util.Map;

public class ServiceSpecification {

    public String version;
    public Info info;
    public Map<String, Resource> resources;
    public Components components;

    public static class Info {
        public String version;
        public String title;
    }

    public static class Resource {

        public String path;
        public String summary;
        public Map<Method, Operation> operations;

        public static class Operation {

            public String path;
            public Method method;
            public String summary;
            public String operationId;
            public List<String> tags;
            public List<Parameter> parameters;
            public RequestBody requestBody;
            public Map<String, Response> responses;

            public static class Parameter {

                public String name;
                public Location location;
                public String description;
                public Boolean required;
                public Schema schema;

                public enum Location {

                    PATH, QUERY, HEADER, COOKIE
                }
            }

            public static class RequestBody {

                public String description;
                public Boolean required;
                public Map<String, Content> content;
            }

            public static class Response {

                public String code;
                public String description;
                public Map<String, Content> content;
            }
        }

        public enum Method {
            GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS, TRACE
        }
    }

    public static class Components {

        public Schemas schemas;

        public static class Schemas {

            public Map<String, Schema> types;
        }
    }

    public static class Content {

        public String mediaType;
        public Schema schema;
    }

    public static class Schema {

        public String title;
        public Type type;
        public Format format;
        public Schema items;
        public List<String> required;
        public Map<String, Schema> properties;
        public String reference;

        public enum Type {
            OBJECT, ARRAY, INTEGER, NUMBER, STRING, BOOLEAN
        }

        public enum Format {
            INT32, INT64, FLOAT, DOUBLE, BYTE, BINARY, DATE, DATE_TIME, PASSWORD
        }
    }
}
