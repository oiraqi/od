package org.galactis.od;

import java.io.File;
import java.io.IOException;

public class ModelGenerator {

    private ModelGenerator() {

    }

    public static void create(String model, String description, String groups, boolean inherit)
            throws IOException, GroupsMissingException {
        File models = new File("./models");
        if (!models.exists()) {
            models.mkdir();
            Util.printToFile("from . import models\n", "./__init__.py");
        }
        File security = new File("./security");
        if (!security.exists() && groups != null) {
            security.mkdir();
            String header = "id,name,model_id:id,group_id:id,perm_read,perm_write,perm_create,perm_unlink\n";
            Util.printToFile(header, "./security/ir.model.access.csv");
        }
        String fileName = Util.getModelShortUnderscoredName(model);
        File modelFile = new File("./models/" + fileName + ".py"); // Path Traversal!!!
        if (modelFile.exists()) {
            if (groups != null) {
                updateAccessRights(model, groups);
            }
            return;
        }
        if (!inherit && groups == null) {
            throw new GroupsMissingException();
        }
        String code = "from odoo import models, fields\n\n\n"
                + "class %s(models.Model):\n\t_%s = '%s'\n\t";
        if (!inherit) {
            code += "_description = '%s'\n\n\tname = fields.Char('Name', required=True)\n\t";
            String className = Util.getClassName(model);
            code = String.format(code, className, "name", model,
                    description == null ? Util.getClassName(model) : description);
        } else {
            code += "\n\t";
            String className = Util.getClassName(model);
            code = String.format(code, className, "inherit", model);
        }

        Util.printToFile(code, modelFile.getPath());
        String init = "from . import " + Util.getModelShortUnderscoredName(model);
        if (!Util.exists("./models/__init__.py") || !Util.in(init, "./models/__init__.py")) {
            Util.appendToFile(init + "\n", "./models/__init__.py");
        }

        if (groups != null) {
            updateAccessRights(model, groups);
        }
    }

    public static void addFields(String model, String fields) throws IOException {
        String fileName = Util.getModelShortUnderscoredName(model);
        String modelPath = "./models/" + fileName + ".py"; // Path Traversal!!!
        String[] fieldz = fields.split(" ");
        for (String field : fieldz) {
            String fieldName = field.split(":")[0];
            if (Util.fieldExists(fieldName, modelPath)) {
                continue;
            }

            String fieldType = field.split(":")[1];

            if (fieldType.equals("mo")) {
                addMany2oneField(field, modelPath);
            } else if (fieldType.equals("om")) {
                addOne2manyField(field, model, modelPath);
            } else if (fieldType.equals("mm")) {
                addMany2manyField(field, modelPath);
            } else {
                addPrimitiveField(field, modelPath);
            }
        }
    }

    private static void addMany2oneField(String field, String modelPath) throws IOException {
        String fieldName = field.split(":")[0];
        String rel = field.split(":")[2];
        String fieldCaption = field.split(":")[3];
        String fieldLine = String.format("%s = fields.Many2one('%s', '%s', required=True)%n\t", fieldName,
                rel,
                fieldCaption);
        Util.appendToFile(fieldLine, modelPath);
    }

    private static void addOne2manyField(String field, String model, String modelPath) throws IOException {
        String fieldName = field.split(":")[0];
        String rel = field.split(":")[2];
        String fieldInverse = Util.getModelShortUnderscoredName(model) + "_id";
        String fieldCaption = field.split(":")[3];
        String fieldLine = String.format("%s = fields.One2many('%s', '%s', '%s')%n\t", fieldName, rel,
                fieldInverse, fieldCaption);
        Util.appendToFile(fieldLine, modelPath);
    }

    private static void addMany2manyField(String field, String modelPath) throws IOException {
        String fieldName = field.split(":")[0];
        String rel = field.split(":")[2];
        String fieldCaption = field.split(":")[3];
        String fieldLine = String.format("%s = fields.Many2many('%s', '%s')%n\t", fieldName, rel, fieldCaption);
        Util.appendToFile(fieldLine, modelPath);
    }

    private static void addPrimitiveField(String field, String modelPath) throws IOException {
        String fieldName = field.split(":")[0];
        String fieldType = field.split(":")[1];
        if (fieldType.equals("c")) {
            fieldType = "Char";
        } else if (fieldType.equals("i")) {
            fieldType = "Integer";
        } else if (fieldType.equals("f")) {
            fieldType = "Float";
        } else if (fieldType.equals("b")) {
            fieldType = "Boolean";
        } else if (fieldType.equals("t")) {
            fieldType = "Text";
        } else if (fieldType.equals("h")) {
            fieldType = "Html";
        }
        String fieldCaption = field.split(":")[2];
        String fieldLine = String.format("%s = fields.%s('%s', required=True)%n\t", fieldName, fieldType,
                fieldCaption);
        Util.appendToFile(fieldLine, modelPath);
    }

    private static void updateAccessRights(String model, String groups) throws IOException {
        String[] groupz = groups.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String groupRight : groupz) {
            String group = groupRight.split(":")[0];
            if (Util.in("access_" + model.replace(".", "_") + "_" + Util.getGroupShortName(group),
                    "./security/ir.model.access.csv")) {
                continue;
            }
            String accessRight = groupRight.split(":")[1];
            String line = String.format("access_%s_%s,access.%s.%s,model_%s,%s,%s,%s,%s,%s", model.replace(".", "_"),
                    Util.getGroupShortName(group),
                    model, Util.getGroupShortName(group), model.replace(".", "_"), group, accessRight.charAt(0),
                    accessRight.charAt(1), accessRight.charAt(2),
                    accessRight.charAt(3));
            sb.append(line + "\n");
        }
        if (sb.length() > 0) {
            sb.append("\n");
        }
        Util.appendToFile(sb.toString(), "./security/ir.model.access.csv");
    }
}
