package ru.smyslokod.gradle.plugin.yaml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class YamlParser {

    public static final Pattern numberPattern = Pattern.compile("(-?)(\\d+)[\\.,]?(\\d+)");

    public YamlRoot parse(List<String> content) {
        List<Node> stack = new ArrayList<>();
        boolean arrayItemOnNextLine = false;
        for (String line : content) {

            if (line.isBlank()) {
                continue;
            }

            int indentation = calculateIndentation(line);

            if (line.charAt(indentation) == '#') {
                continue;
            }

            String body = line.trim();

            arrayItemOnNextLine = body.equals("-");

            boolean arrayItem = body.startsWith("- ");
            int itemIndentation = indentation;
            if (arrayItem) {
                body = body.substring(2);
                itemIndentation += 2 + calculateIndentation(body);
                body = body.trim();
            }

            int level = findLevel(stack, indentation);
            int diff = level - stack.size() + 1;
            for (int i = stack.size() - 1; i > level; i--) {
                stack.remove(i);
            }

            Node parent = stack.isEmpty() ? null : stack.get(stack.size() - 1);

            if ((arrayItem || arrayItemOnNextLine) && diff >= 0) {
                YamlRoot array = new YamlArray();
                if (parent != null) {
                    ((YamlObject) parent.getNode()).setField(parent.getOpenKey(), array);
                    parent.setOpenKey(null);
                }
                parent = new Node(array, indentation);
                stack.add(parent);
            }
            if (arrayItemOnNextLine) {
                continue;
            }

            String key = null;
            String value = null;
            boolean keyOnly = body.endsWith(":");
            boolean mapping = false;
            if (keyOnly) {
                key = body.substring(0, body.length() - 1).trim();
            } else {
                String[] mappingContent = body.split(": ");
                mapping = mappingContent.length > 1;
                if (mapping) {
                    key = mappingContent[0].trim();
                    value = mappingContent[1].trim();
                } else {
                    value = body;
                }
            }

            if ((keyOnly || mapping) && diff > 0) {
                YamlObject object = new YamlObject();
                if (parent != null) {
                    if (parent.getNode() instanceof YamlArray) {
                        ((YamlArray) parent.getNode()).add(object);
                    } else if (parent.getNode() instanceof YamlObject) {
                        ((YamlObject) parent.getNode()).setField(parent.getOpenKey(), object);
                        parent.setOpenKey(null);
                    }
                }
                parent = new Node(object, itemIndentation);
                stack.add(parent);
            }
            if (keyOnly) {
                parent.setOpenKey(key);
                continue;
            }

            YamlRoot scalarValue = extractScalar(value);

            if (parent.getNode() instanceof YamlArray) {
                ((YamlArray) parent.getNode()).add(scalarValue);
            } else if (parent.getNode() instanceof YamlObject) {
                ((YamlObject) parent.getNode()).setField(key, scalarValue);
            } else {
                throw new YamlParserException("Parent is nor array, nor object of line: " + line);
            }

            if (parent == null && stack.isEmpty()) {
                stack.add(new Node(scalarValue, 0));
            }
        }
        return stack.isEmpty() ? new YamlNull() : stack.get(0).getNode();
    }

// throw new YamlParserException("New item without value for a previous one: " + line);
// throw new YamlParserException("Illegal indentation on line: " + line);
// throw new YamlParserException("Parent is nor array, nor object of line: " + line);
// throw new YamlParserException("Uncompleted object with no value for key " + currentKey + " at the end of file");
// throw new YamlParserException("Uncompleted array item at the end of file");

    public YamlRoot extractScalar(String value) {
        YamlRoot scalarValue;
        if ("null".equalsIgnoreCase(value)) {
            scalarValue = new YamlNull();
        } else if ("true".equalsIgnoreCase(value)) {
            scalarValue = new YamlBoolean(true);
        } else if ("false".equalsIgnoreCase(value)) {
            scalarValue = new YamlBoolean(false);
        } else if (numberPattern.matcher(value).matches()) {
            scalarValue = new YamlNumber(new BigDecimal(value));
        } else {
            // quotes
            scalarValue = new YamlString(value);
        }
        return scalarValue;
    }

    public int findLevel(List<Node> stack, int indentation) {
        if (stack.isEmpty()) {
            return 0;
        }
        int previousIndentation = stack.get(stack.size() - 1).getIndentation();
        if (indentation > previousIndentation) {
            return stack.size();
        }
        for (int i = stack.size() - 1; i >= 0; i--) {
            Node node = stack.get(i);
            if (indentation > node.getIndentation()) {
                return -1;
            }
            if (indentation == stack.get(i).getIndentation()) {
                return i;
            }
        }
        return -1;
    }

    public int calculateIndentation(String line) {
        int indentation = 0;
        for (char element : line.toCharArray()) {
            if (element != ' ') {
                break;
            } else {
                indentation++;
            }
        }
        return indentation;
    }

    public static class Node {

        public final YamlRoot node;
        public final int indentation;
        public String openKey;

        public Node(YamlRoot node, int indentation) {
            this.node = node;
            this.indentation = indentation;
        }

        public YamlRoot getNode() {
            return node;
        }

        public int getIndentation() {
            return indentation;
        }

        public void setOpenKey(String openKey) {
            this.openKey = openKey;
        }

        public String getOpenKey() {
            return openKey;
        }
    }
}
