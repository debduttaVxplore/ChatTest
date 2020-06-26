package com.coderusk.chattest;

import java.util.ArrayList;

public class Tefinition {
    ////////////////////////
    static final String INT = "INT";
    static final String INTEGER = "INTEGER";
    static final String TINYINT = "TINYINT";
    static final String SMALLINT = "SMALLINT";
    static final String MEDIUMINT = "MEDIUMINT";
    static final String BIGINT = "BIGINT";
    static final String UNSIGNED_BIG_INT = "UNSIGNED BIG INT";
    static final String INT2 = "INT2";
    static final String INT8 = "INT8";
    static final String CHARACTER = "CHARACTER";
    static final String VARCHAR = "VARCHAR";
    static final String VARYING_CHARACTER = "VARYING CHARACTER";
    static final String NCHAR = "NCHAR";
    static final String NATIVE_CHARACTER = "NATIVE CHARACTER";
    static final String NVARCHAR = "NVARCHAR";
    static final String TEXT = "TEXT";
    static final String CLOB = "CLOB";
    static final String BLOB = "BLOB";
    static final String REAL = "REAL";
    static final String DOUBLE = "DOUBLE";
    static final String DOUBLE_PRECISION = "DOUBLE PRECISION";
    static final String FLOAT = "FLOAT";
    static final String NUMERIC = "NUMERIC";
    static final String DECIMAL = "DECIMAL";
    static final String BOOLEAN = "BOOLEAN";
    static final String DATE = "DATE";
    static final String DATETIME = "DATETIME";
    ///////////////////
    static final int CONSTRAINT = 0;
    static final int PRIMARY_KEY = 1;
    static final int PRIMARY_KEY_ASC = 2;
    static final int PRIMARY_KEY_DESC = 2;
    static final int AUTOINCREMENT = 3;
    static final int NOT_NULL = 4;
    static final int UNIQUE = 5;
    static final int CHECK = 6;
    static final int DEFAULT = 7;
    static final int COLLATE = 8;
    static final int FOREIGN = 9;
    static final int GENERATED_ALWAYS = 10;
    static final int AS = 11;
    static final int STORED = 12;
    static final int VIRTUAL = 12;
    class Column
    {
        String name = "";
        String type = "";
        //////////////////

        //////////////////
        String[] constraints = {
                "",/**CONSTRAINT**/
                "",/**PRIMARY_KEY**/
                "",/**PRIMARY_KEY_ASC|PRIMARY_KEY_DESC**/
                "",/**AUTOINCREMENT**/
                "",/**NOT_NULL**/
                "",/**UNIQUE**/
                "",/**CHECK**/
                "",/**DEFAULT**/
                "",/**COLLATE**/
                "",/**FOREIGN**/
                "",/**GENERATED_ALWAYS**/
                "",/**AS**/
                ""/**STORED|VIRTUAL**/
        };

        public Column(String name)
        {
            this.name = name;
        }

        public String getDef() {
            return
                    name+" "+type+" "+
                    constraints[CONSTRAINT]+" "+
                    constraints[PRIMARY_KEY]+" "+
                    constraints[PRIMARY_KEY_ASC]+" "+
                    constraints[AUTOINCREMENT]+" "+
                    constraints[NOT_NULL]+" "+
                    constraints[UNIQUE]+" "+
                    constraints[CHECK]+" "+
                    constraints[DEFAULT]+" "+
                    constraints[COLLATE]+" "+
                    constraints[FOREIGN]+" "+
                    constraints[GENERATED_ALWAYS]+" "+
                    constraints[AS]+" "+
                    constraints[STORED];
        }
    }
    ////////////////////////
    Column current_column = null;
    ArrayList<Column> columns = new ArrayList<>();
    private Tefinition() {
    }

    public static Tefinition create()
    {
        return new Tefinition();
    }

    static final int CREATE = 0;
    static final int TEMP = 1;
    static final int TEMPORARY = 1;
    static final int TABLE = 2;
    static final int IF_NOT_EXISTS = 3;
    static final int SCHEMA_NAME_DOT = 4;
    static final int TABLE_NAME = 5;
    static final int PAR_START = 6;
    static final int COLUMN_DEF = 7;
    static final int COMMA = 8;
    static final int TABLE_CONSTRAINT = 9;
    static final int PAR_END = 10;
    static final int WITHOUT_ROWID = 11;
    static final int TABLE_AS = 12;
    String options[] = {
            "CREATE",/**0=CREATE**/
            "",/**1 = TEMP|TEMPORARY**/
            "TABLE",/**2 = TABLE**/
            "",/**3 = IF NOT EXISTS**/
            "",/**4 = SCHEMA NAME DOT**/
            "",/**5 = TABLE NAME**/
            "",/**6 = (**/
            "",/**7 = COLUMN_DEF**/
            "",/**8 = ,**/
            "",/**9 = TABLE CONSTRAINT**/
            "",/**10 = )**/
            "",/**11 = WITHOUT ROWID**/
            "",/**12 = TABLE_AS**/
    };

    public String queryString()
    {
        if(current_column!=null)
        {
            columns.add(current_column);
            current_column=null;
        }
        options[COLUMN_DEF] = getColumnDef();
        ////////////////////////////////////////
        String ret =options[CREATE]+" "+
        options[TEMP]+" "+
        options[TABLE]+" "+
        options[IF_NOT_EXISTS]+" "+
        options[SCHEMA_NAME_DOT]+" "+
        options[TABLE_NAME]+" "+
               getTableEnding() ;
        while (ret.contains("  "))
        {
            ret = ret.replaceAll("  "," ");
        }
        return ret;
    }

    private String getTableEnding() {
        if(!options[PAR_START].isEmpty())
        {
            return getColumnDefRoute();
        }
        else
        {
            return getAsDefRoute();
        }
    }

    private String getAsDefRoute() {
        return options[AS]+" ";
    }

    private String getColumnDefRoute() {
        return
                options[PAR_START]+
                options[COLUMN_DEF]+
                        getComma()+
                options[TABLE_CONSTRAINT]+
                options[PAR_END]+" "+
                options[WITHOUT_ROWID];
    }

    private String getComma() {
        if((!options[COLUMN_DEF].isEmpty())&&
                (!options[AS].isEmpty()))
        {
            options[COMMA] = ",";
            return options[COMMA];
        }
        else
        {
            return "";
        }
    }

    private String getColumnDef() {
        if(columns!=null)
        {
            if(columns.size()>0)
            {
                ArrayList<String> columnDefs = new ArrayList<>();
                int len = columns.size();
                for(int i=0;i<len;++i)
                {
                    Column column = columns.get(i);
                    String columnDef = column.getDef();
                    columnDefs.add(columnDef);
                }
                return android.text.TextUtils.join(",", columnDefs);
            }
        }
        return "";
    }

    public Tefinition TEMP()
    {
        options[TEMP] = "TEMP";
        return this;
    }

    public Tefinition TEMPORARY()
    {
        options[TEMPORARY] = "TEMPORARY";
        return this;
    }

    public Tefinition table()
    {
        return this;
    }

    public Tefinition if_not_exists()
    {
        options[IF_NOT_EXISTS] = "IF NOT EXISTS";
        return this;
    }

    public Tefinition schema_name(String schema)
    {
        options[SCHEMA_NAME_DOT] = schema+ ".";
        return this;
    }

    public Tefinition name(String table)
    {
        options[TABLE_NAME] = table;
        return this;
    }

    public Tefinition column(String column)
    {
        options[PAR_START] = "(";
        options[PAR_END] = ")";
        if(current_column!=null)
        {
            columns.add(current_column);
        }
        current_column = new Column(column);
        return this;
    }

    public Tefinition table_constraint(String constraint)
    {
        options[TABLE_CONSTRAINT] = ","+constraint;
        options[PAR_END] = ")";
        return this;
    }

    public Tefinition without_rowid()
    {
        options[WITHOUT_ROWID] = "WITHOUT ROWID";
        return this;
    }

    public Tefinition table_as(String asStatement)
    {
        options[TABLE_AS] = "AS "+asStatement;
        return this;
    }



    public Tefinition INT(){current_column.type = INT;return this;}
    public Tefinition INTEGER(){current_column.type = INTEGER;return this;}
    public Tefinition TINYINT(){current_column.type = TINYINT;return this;}
    public Tefinition SMALLINT(){current_column.type = SMALLINT;return this;}
    public Tefinition MEDIUMINT(){current_column.type = MEDIUMINT;return this;}
    public Tefinition BIGINT(){current_column.type = BIGINT;return this;}
    public Tefinition UNSIGNED_BIG_INT(){current_column.type = UNSIGNED_BIG_INT;return this;}
    public Tefinition INT2(){current_column.type = INT2;return this;}
    public Tefinition INT8(){current_column.type = INT8;return this;}
    public Tefinition CHARACTER(){current_column.type = CHARACTER;return this;}
    public Tefinition VARCHAR(){current_column.type = VARCHAR;return this;}
    public Tefinition VARYING_CHARACTER(){current_column.type = VARYING_CHARACTER;return this;}
    public Tefinition NCHAR(){current_column.type = NCHAR;return this;}
    public Tefinition NATIVE_CHARACTER(){current_column.type = NATIVE_CHARACTER;return this;}
    public Tefinition NVARCHAR(){current_column.type = NVARCHAR;return this;}
    public Tefinition TEXT(){current_column.type = TEXT;return this;}
    public Tefinition CLOB(){current_column.type = CLOB;return this;}
    public Tefinition BLOB(){current_column.type = BLOB;return this;}
    public Tefinition REAL(){current_column.type = REAL;return this;}
    public Tefinition DOUBLE(){current_column.type = DOUBLE;return this;}
    public Tefinition DOUBLE_PRECISION(){current_column.type = DOUBLE_PRECISION;return this;}
    public Tefinition FLOAT(){current_column.type = FLOAT;return this;}
    public Tefinition NUMERIC(){current_column.type = NUMERIC;return this;}
    public Tefinition DECIMAL(){current_column.type = DECIMAL;return this;}
    public Tefinition BOOLEAN(){current_column.type = BOOLEAN;return this;}
    public Tefinition DATE(){current_column.type = DATE;return this;}
    public Tefinition DATETIME(){current_column.type = DATETIME;return this;}

    public Tefinition CONSTRAINT(String constraint) {
        current_column.constraints[CONSTRAINT] = "CONSTRAINT "+constraint;
        return this;
    }

    public Tefinition PRIMARY_KEY() {
        current_column.constraints[PRIMARY_KEY] = "PRIMARY KEY";
        return this;
    }

    public Tefinition PRIMARY_KEY_ASC() {
        current_column.constraints[PRIMARY_KEY_ASC] = "ASC";
        return this;
    }

    public Tefinition PRIMARY_KEY_DESC() {
        current_column.constraints[PRIMARY_KEY_DESC] = "DESC";
        return this;
    }

    public Tefinition AUTOINCREMENT() {
        current_column.constraints[AUTOINCREMENT] = "AUTOINCREMENT";
        return this;
    }

    public Tefinition NOT_NULL() {
        current_column.constraints[NOT_NULL] = "NOT NULL";
        return this;
    }

    public Tefinition UNIQUE() {
        current_column.constraints[UNIQUE] = "UNIQUE";
        return this;
    }

    public Tefinition CHECK(String expr) {
        current_column.constraints[CHECK] = "CHECK "+expr;
        return this;
    }

    public Tefinition DEFAULT(String defaultValue) {
        current_column.constraints[DEFAULT] = "DEFAULT "+defaultValue;
        return this;
    }

    public Tefinition COLLATE(String collate) {
        current_column.constraints[COLLATE] = "COLLATE "+collate;
        return this;
    }

    public Tefinition FOREIGN(String foreignKeyClause) {
        current_column.constraints[FOREIGN] = foreignKeyClause;
        return this;
    }

    public Tefinition GENERATED_ALWAYS() {
        current_column.constraints[GENERATED_ALWAYS] = "GENERATED ALWAYS";
        return this;
    }

    public Tefinition AS(String expr) {
        current_column.constraints[AS] = "AS "+expr;
        return this;
    }

    public Tefinition STORED() {
        current_column.constraints[STORED] = "STORED";
        return this;
    }

    public Tefinition VIRTUAL() {
        current_column.constraints[VIRTUAL] = "VIRTUAL";
        return this;
    }

}
