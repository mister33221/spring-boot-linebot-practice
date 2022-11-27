package com.example.linebotpractice.model;

public enum FlexMessageDemo {

    DEMO1, DEMO2, DEMO3 ;

    public static String getFlexMessageDemo(FlexMessageDemo flexMessageDemo) {
        switch (flexMessageDemo) {
            case DEMO1:
                return "" +
            "    {" +
                    "      \"type\": \"flex\"," +
                    "      \"altText\": \"This is a Flex Message\"," +
                    "      \"contents\": {" +
                    "        \"type\": \"bubble\"," +
                    "        \"body\": {" +
                    "          \"type\": \"box\"," +
                    "          \"layout\": \"vertical\"," +
                    "          \"contents\": [" +
                    "            {" +
                    "              \"type\": \"text\"," +
                    "              \"text\": \"Line bot flex message test\"," +
                    "              \"wrap\": true" +
                    "            }," +
                    "            {" +
                    "              \"type\": \"text\"," +
                    "              \"text\": \"Hello, World!\"" +
                    "            }" +
                    "          ]" +
                    "        }" +
                    "      }" +
                    "    }" +
                    "  ";
            case DEMO2:
                return "{" +
                        "      \"type\": \"flex\"," +
                        "      \"altText\": \"This is a Flex Message\"," +
                        "      \"contents\": {" +
                        "    \"type\": \"bubble\"," +
                        "    \"hero\": {" +
                        "      \"type\": \"image\"," +
                        "      \"url\": \"https://scdn.line-apps.com/n/channel_devcenter/img/fx/01_1_cafe.png\"," +
                        "      \"size\": \"full\"," +
                        "      \"aspectRatio\": \"20:13\"," +
                        "      \"aspectMode\": \"cover\"" +
                        "    }," +
                        "    \"body\": {" +
                        "      \"type\": \"box\"," +
                        "      \"layout\": \"vertical\"," +
                        "      \"contents\": [" +
                        "        {" +
                        "          \"type\": \"text\"," +
                        "          \"text\": \"Kai\"," +
                        "          \"size\": \"xl\"," +
                        "          \"weight\": \"bold\"," +
                        "          \"color\": \"#0099ff\"" +
                        "        }," +
                        "        {" +
                        "          \"type\": \"box\"," +
                        "          \"layout\": \"vertical\"," +
                        "          \"contents\": [" +
                        "            {" +
                        "              \"type\": \"text\"," +
                        "              \"text\": \"塵世中一個迷途小工程師\"" +
                        "            }" +
                        "          ]" +
                        "        }," +
                        "        {" +
                        "          \"type\": \"box\"," +
                        "          \"layout\": \"baseline\"," +
                        "          \"contents\": [" +
                        "            {" +
                        "              \"type\": \"icon\"," +
                        "              \"url\": \"https://i.imgur.com/4G4LFjU.png\"" +
                        "            }," +
                        "            {" +
                        "              \"type\": \"text\"," +
                        "              \"text\": \"example@gmail.com\"" +
                        "            }" +
                        "          ]" +
                        "        }," +
                        "        {" +
                        "          \"type\": \"box\"," +
                        "          \"layout\": \"baseline\"," +
                        "          \"contents\": [" +
                        "            {" +
                        "              \"type\": \"icon\"," +
                        "              \"url\": \"https://i.imgur.com/gzfW90Q.png\"" +
                        "            }," +
                        "            {" +
                        "              \"type\": \"text\"," +
                        "              \"text\": \"https://mister33221.github.io/index.html\"" +
                        "            }" +
                        "          ]" +
                        "        }" +
                        "      ]" +
                        "    }," +
                        "    \"footer\": {" +
                        "      \"type\": \"box\"," +
                        "      \"layout\": \"vertical\"," +
                        "      \"contents\": [" +
                        "        {" +
                        "          \"type\": \"button\"," +
                        "          \"action\": {" +
                        "            \"type\": \"uri\"," +
                        "            \"label\": \"Website\"," +
                        "            \"uri\": \"https://mister33221.github.io/index.html\"" +
                        "          }" +
                        "        }" +
                        "      ]" +
                        "    }" +
                        "    }" +
                        "  }";

            case DEMO3:
                return "DEMO3";
            default:
                return "DEMO1";
        }
    }

}
