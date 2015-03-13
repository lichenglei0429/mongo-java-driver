/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb;

import org.bson.BsonDocument;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.bson.json.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public final class JsonPoweredTestHelper {

    public static BsonDocument getTestDocument(final File file) throws IOException {
        return new BsonDocumentCodec().decode(new JsonReader(getFileAsString(file)), DecoderContext.builder().build());
    }

    public static List<File> getTestFiles(final String resourcePath) throws URISyntaxException {
        List<File> files = new ArrayList<File>();
        addFilesFromDirectory(new File(JsonPoweredTestHelper.class.getResource(resourcePath).toURI()), files);
        return files;
    }

    private static String getFileAsString(final File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

    private static void addFilesFromDirectory(final File directory, final List<File> files) {
        for (String fileName : directory.list()) {
            File file = new File(directory, fileName);
            if (file.isDirectory()) {
                addFilesFromDirectory(file, files);
            } else if (file.getName().endsWith(".json")) {
                files.add(file);
            }
        }
    }

    private JsonPoweredTestHelper() {
    }
}