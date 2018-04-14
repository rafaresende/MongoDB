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
import static com.mongodb.m101j.util.Helpers.printJson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class HW3_1 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("school");
        MongoCollection<Document> collection = database.getCollection("students");
        
        MongoCursor<Document> cursor = collection.find().iterator();
        
        int index = 0;
        
        try {
            while (cursor.hasNext()) {
                Document cur = cursor.next();
                ArrayList<Document> doc = (ArrayList<Document>) cur.get("scores");
                ListIterator<Document> docIterator = doc.listIterator();
                double menor = 0;
                index = 0;
                while(docIterator.hasNext()) {
                	Document d = docIterator.next();
                	String tipo = d.getString("type");
                	if(tipo.equals("homework")){
                		double score = d.getDouble("score");
                		if(index == 0) {
                			menor = score;
                			index = 1;
                		}
                		if(score<menor) {
                			menor = score;
                		}
                	}
                	
                	if(!docIterator.hasNext() & d.getDouble("score")==menor) {
                		docIterator.remove();
                		cur.put("scores", doc);                	
                	} else if (!docIterator.hasNext()) {
                		System.out.println("aahj");
                		d = docIterator.previous();
                		d = docIterator.previous();
                		docIterator.remove();
                		cur.put("scores", doc);
                		break;
                	}
                }
                int id = cur.getInteger("_id");
                collection.updateOne(eq("_id",id), new Document("$set",  cur));
              printJson(cur);
                
                System.out.println(menor);
            }
        } finally {
            cursor.close();
            client.close();
        }
    }
}
