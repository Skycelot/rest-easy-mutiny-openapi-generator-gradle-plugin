package ru.smyslokod.gradle.plugin.model;

import java.util.ArrayList;
import java.util.HashMap;

import ru.smyslokod.gradle.plugin.yaml.YamlArray;
import ru.smyslokod.gradle.plugin.yaml.YamlBoolean;
import ru.smyslokod.gradle.plugin.yaml.YamlObject;
import ru.smyslokod.gradle.plugin.yaml.YamlRoot;
import ru.smyslokod.gradle.plugin.yaml.YamlString;

public class ServiceSpecificationYamlReader {

    private final EnumValuator enumValuator;

    public ServiceSpecificationYamlReader(EnumValuator enumValuator) {
        this.enumValuator = enumValuator;
    }

    public ServiceSpecification readYaml(YamlRoot openApiSchema) {

        YamlObject schemaObject = (YamlObject) openApiSchema;
        ServiceSpecification specification = new ServiceSpecification();

        specification.version = (
                (YamlString) schemaObject
                        .getField("openapi")
                        .orElseThrow(IllegalArgumentException::new)
        )
                .getValue();

        YamlObject infoObject = (YamlObject) schemaObject
                .getField("info")
                .orElseThrow(IllegalArgumentException::new);
        specification.info = new ServiceSpecification.Info();
        specification.info.version = (
                (YamlString) infoObject
                        .getField("version")
                        .orElseThrow(IllegalArgumentException::new)
        )
                .getValue();
        specification.info.title = (
                (YamlString) infoObject
                        .getField("title")
                        .orElseThrow(IllegalArgumentException::new)
        )
                .getValue();

        YamlObject pathsObject = (YamlObject) schemaObject
                .getField("paths")
                .orElseThrow(IllegalArgumentException::new);
        specification.resources = new HashMap<>();
        pathsObject.spliterator().forEachRemaining(path -> {

            YamlObject pathObject = (YamlObject) pathsObject
                    .getField(path)
                    .orElseThrow(IllegalStateException::new);
            ServiceSpecification.Resource resource = new ServiceSpecification.Resource();
            specification.resources.put(path, resource);
            resource.path = path;

            pathObject
                    .getField("description")
                    .ifPresent(summary ->
                            resource.summary = ((YamlString) summary).getValue()
                    );

            resource.operations = new HashMap<>();
            pathObject
                    .getField(ServiceSpecification.Resource.Method.GET.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation getOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.GET
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.GET, getOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.POST.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation postOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.POST
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.POST, postOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.PUT.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation putOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.PUT
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.PUT, putOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.PATCH.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation patchOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.PATCH
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.PATCH, patchOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.DELETE.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation deleteOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.DELETE
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.DELETE, deleteOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.HEAD.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation headOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.HEAD
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.HEAD, headOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.OPTIONS.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation optionsOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.OPTIONS
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.OPTIONS, optionsOperation);
                    });
            pathObject
                    .getField(ServiceSpecification.Resource.Method.TRACE.name().toLowerCase())
                    .ifPresent(operation -> {
                        ServiceSpecification.Resource.Operation traceOperation = readOperation(
                                (YamlObject) operation,
                                path,
                                ServiceSpecification.Resource.Method.TRACE
                        );
                        resource.operations.put(ServiceSpecification.Resource.Method.TRACE, traceOperation);
                    });
        });

        schemaObject
                .getField("components")
                .flatMap(componentsObject ->
                        ((YamlObject) componentsObject)
                                .getField("schemas")
                )
                .ifPresent(schemas -> {
                    YamlObject schemasObject = (YamlObject) schemas;
                    specification.components = new ServiceSpecification.Components();
                    specification.components.schemas = new ServiceSpecification.Components.Schemas();
                    specification.components.schemas.types = new HashMap<>();
                    schemasObject.spliterator().forEachRemaining(typeName -> {

                        YamlObject typeObject = (YamlObject) schemasObject
                                .getField(typeName)
                                .orElseThrow(IllegalStateException::new);
                        ServiceSpecification.Schema schema = readSchema(typeObject, typeName);
                        specification.components.schemas.types.put(typeName, schema);
                    });
                });
        return specification;
    }

    public ServiceSpecification.Resource.Operation readOperation(
            YamlObject operationObject,
            String path,
            ServiceSpecification.Resource.Method method
    ) {

        ServiceSpecification.Resource.Operation operation = new ServiceSpecification.Resource.Operation();
        operation.path = path;
        operation.method = method;

        operationObject
                .getField("summary")
                .ifPresent(summary ->
                        operation.summary = ((YamlString) summary).getValue()
                );
        operationObject
                .getField("operationId")
                .ifPresent(operationId ->
                        operation.operationId = ((YamlString) operationId).getValue()
                );

        operation.parameters = new ArrayList<>();
        operationObject
                .getField("parameters")
                .ifPresent(parameters -> ((YamlArray) parameters).spliterator()
                        .forEachRemaining(parameterItem -> {
                            YamlObject parameterObject = (YamlObject) parameterItem;
                            ServiceSpecification.Resource.Operation.Parameter parameter
                                    = new ServiceSpecification.Resource.Operation.Parameter();
                            operation.parameters.add(parameter);

                            parameter.name = (
                                    (YamlString) parameterObject
                                            .getField("name")
                                            .orElseThrow(IllegalArgumentException::new)
                            )
                                    .getValue();

                            parameter.location = enumValuator.valueOf(
                                            ServiceSpecification.Resource.Operation.Parameter.Location.class,
                                            (
                                                    (YamlString) parameterObject
                                                            .getField("in")
                                                            .orElseThrow(IllegalArgumentException::new)
                                            )
                                                    .getValue()
                                    )
                                    .orElseThrow(IllegalArgumentException::new);

                            parameterObject
                                    .getField("required")
                                    .ifPresent(parameterRequired ->
                                            parameter.required = ((YamlBoolean) parameterRequired).getValue()
                                    );

                            parameterObject
                                    .getField("description")
                                    .ifPresent(parameterDescription ->
                                            parameter.description = ((YamlString) parameterDescription).getValue()
                                    );

                            parameterObject
                                    .getField("schema")
                                    .ifPresent(parameterSchema ->
                                            parameter.schema = readSchema((YamlObject) parameterSchema, null)
                                    );
                        }));


        // request body

        YamlObject responsesObject = (YamlObject) operationObject
                .getField("responses")
                .orElseThrow(IllegalArgumentException::new);
        operation.responses = new HashMap<>();
        responsesObject.spliterator().forEachRemaining(responseCode -> {
            YamlObject responseObject = (YamlObject) responsesObject
                    .getField(responseCode)
                    .orElseThrow(IllegalStateException::new);
            ServiceSpecification.Resource.Operation.Response response
                    = new ServiceSpecification.Resource.Operation.Response();
            operation.responses.put(responseCode, response);
            response.code = responseCode;

            response.description = (
                    (YamlString) responseObject
                            .getField("description")
                            .orElseThrow(IllegalArgumentException::new)
            )
                    .getValue();

            response.content = new HashMap<>();
            responseObject
                    .getField("content")
                    .ifPresent(contentMap -> {
                        YamlObject contentMapObject = (YamlObject) contentMap;
                        contentMapObject.spliterator()
                                .forEachRemaining(mediaType ->
                                        response.content.put(
                                                mediaType,
                                                readContent(
                                                        (YamlObject) contentMapObject
                                                                .getField(mediaType)
                                                                .orElseThrow(IllegalStateException::new),
                                                        mediaType
                                                )
                                        )
                                );
                    });
        });
        return operation;
    }

    public ServiceSpecification.Schema readSchema(YamlObject schemaObject, String title) {
        ServiceSpecification.Schema schema = new ServiceSpecification.Schema();

        schema.title = schemaObject
                .getField("title")
                .map(titleProperty -> ((YamlString) titleProperty).getValue())
                .orElse(title);

        schemaObject
                .getField("type")
                .ifPresent(schemaType ->
                        schema.type = enumValuator.valueOf(
                                        ServiceSpecification.Schema.Type.class,
                                        ((YamlString) schemaType).getValue()
                                )
                                .orElseThrow(IllegalArgumentException::new));

        schemaObject
                .getField("format")
                .ifPresent(schemaFormat ->
                        schema.format = enumValuator.valueOf(
                                        ServiceSpecification.Schema.Format.class,
                                        ((YamlString) schemaFormat).getValue()
                                )
                                .orElseThrow(IllegalArgumentException::new)
                );

        schemaObject
                .getField("$ref")
                .ifPresent(schemaReference ->
                        schema.reference = ((YamlString) schemaReference).getValue()
                );

        schema.properties = new HashMap<>();
        schemaObject
                .getField("properties")
                .ifPresent(properties -> {
                    YamlObject propertiesObject = (YamlObject) properties;
                    propertiesObject.spliterator().forEachRemaining(propertyTitle ->
                            schema.properties.put(
                                    propertyTitle,
                                    readSchema(
                                            (YamlObject) propertiesObject
                                                    .getField(propertyTitle)
                                                    .orElseThrow(IllegalStateException::new),
                                            propertyTitle
                                    )
                            )
                    );
                });

        schema.required = new ArrayList<>();
        schemaObject
                .getField("required")
                .ifPresent(required -> ((YamlArray) required).spliterator().forEachRemaining(requiredProperty ->
                                schema.required.add(((YamlString) requiredProperty).getValue())
                        )
                );

        return schema;
    }

    public ServiceSpecification.Content readContent(YamlObject contentObject, String mediaType) {
        ServiceSpecification.Content content = new ServiceSpecification.Content();
        content.mediaType = mediaType;

        contentObject
                .getField("schema")
                .ifPresent(contentSchema ->
                        content.schema = readSchema(
                                (YamlObject) contentSchema,
                                null
                        )
                );

        return content;
    }
}
