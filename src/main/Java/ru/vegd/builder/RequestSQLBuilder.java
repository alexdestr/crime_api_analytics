package ru.vegd.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestSQLBuilder {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(RequestSQLBuilder.class.getName());

    private static final String startTag = "<";
    private static final String endTag = ">";

    private String startSqlQuery;
    private String finalSqlQuery;

    private Map valueMap = new HashMap();

    public RequestSQLBuilder setSQL(String startSQL) {
        this.startSqlQuery = startSQL;
        return this;
    }

    public RequestSQLBuilder setValues(Map values) {
        this.valueMap = values;
        return this;
    }

    public String build() {
        Pattern pattern = Pattern.compile(startTag + "(.*?)" + endTag);
        Matcher matcher = pattern.matcher(startSqlQuery);
        Integer count = 0;

        while (matcher.find()) {
            String matchedString = (String) startSqlQuery.subSequence(matcher.start(), matcher.end());
            String matchedStringWithoutTags = matchedString
                    .replaceAll(startTag, "")
                    .replaceAll(endTag, "");
            if (count == 0) {
                finalSqlQuery = startSqlQuery.replaceAll(matchedString, (String) valueMap.get(matchedStringWithoutTags));
            } else {
                finalSqlQuery = finalSqlQuery.replaceAll(matchedString, (String) valueMap.get(matchedStringWithoutTags));
            }
            count++;
        }

        return finalSqlQuery;
    }

}
