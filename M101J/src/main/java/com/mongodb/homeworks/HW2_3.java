/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.homeworks;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.ascending;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class HW2_3 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("students");
        MongoCollection<Document> collection = database.getCollection("grades");


        Bson projection = excludeId();
        Bson sort = ascending("student_id","score");

       /* List<Document> all = collection.find(new Document("type","homework"))
                                       .projection(projection)
                                       .sort(sort)
                                       .into(new ArrayList<Document>());*/
        
        MongoCursor<Document> cursor = collection.find(new Document("type","homework"))
                						.projection(projection)
                						.sort(sort).iterator();
        
        int id = -1;
        
        try {
            while (cursor.hasNext()) {
                Document cur = cursor.next();
                if(cur.getInteger("student_id") != id) {
                	collection.deleteOne(and(eq("student_id",cur.getInteger("student_id")),
                			eq("score", cur.getDouble("score"))));
                	id = cur.getInteger("student_id");
                }
                	
                //printJson(cur);
            }
        } finally {
            cursor.close();
            client.close();
        }
        
        /*for (Document cur : all) {
            printJson(cur);
        }*/
    }
}
