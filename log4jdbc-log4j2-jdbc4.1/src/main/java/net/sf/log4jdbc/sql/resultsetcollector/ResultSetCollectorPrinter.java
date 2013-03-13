/**
 * Copyright 2010 Tim Azzopardi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package net.sf.log4jdbc.sql.resultsetcollector;

import java.util.ArrayList;
import java.util.List;

/***
 * @author Tim Azzopardi
 * @author Mathieu Seppey 
 * 
 * Update : changed printResultSet into getResultSetToPrint
 * 
 */

public class ResultSetCollectorPrinter {

  private List<String> result ;

  public ResultSetCollectorPrinter() {

  }

  public List<String> getResultSetToPrint(ResultSetCollector resultSetCollector) {
    
    this.result = new ArrayList<String>();

    int columnCount = resultSetCollector.getColumnCount();
    int maxLength[] = new int[columnCount];

    for (int column = 1; column <= columnCount; column++) {
      maxLength[column - 1] = resultSetCollector.getColumnName(column)
          .length();
    }
    if (resultSetCollector.getRows() != null) {
      for (List<Object> printRow : resultSetCollector.getRows()) {
        int colIndex = 0;
        for (Object v : printRow) {
          if (v != null) {
            int length = v.toString().length();
            if (length > maxLength[colIndex]) {
              maxLength[colIndex] = length;
            }
          }
          colIndex++;
        }
      }
    }
    for (int column = 1; column <= columnCount; column++) {
      maxLength[column - 1] = maxLength[column - 1] + 1;
    }

    print("|");
    for (int column = 1; column <= columnCount; column++) {
      print(padRight("-", maxLength[column - 1]).replaceAll(" ", "-")
          + "|");
    }
    println();
    print("|");
    for (int column = 1; column <= columnCount; column++) {
      print(padRight(resultSetCollector.getColumnName(column),
          maxLength[column - 1])
          + "|");
    }
    println();
    print("|");
    for (int column = 1; column <= columnCount; column++) {
      print(padRight("-", maxLength[column - 1]).replaceAll(" ", "-")
          + "|");
    }
    println();
    if (resultSetCollector.getRows() != null) {
      for (List<Object> printRow : resultSetCollector.getRows()) {
        int colIndex = 0;
        print("|");
        for (Object v : printRow) {
          print(padRight(v == null ? "null" : v.toString(),
              maxLength[colIndex])
              + "|");
          colIndex++;
        }
        println();
      }
    }
    print("|");
    for (int column = 1; column <= columnCount; column++) {
      print(padRight("-", maxLength[column - 1]).replaceAll(" ", "-")
          + "|");
    }
    println();
    
    resultSetCollector.reset();
    
    return this.result ;
    
  }

  public static String padRight(String s, int n) {
    return String.format("%1$-" + n + "s", s);
  }

  public static String padLeft(String s, int n) {
    return String.format("%1$#" + n + "s", s);
  }

  void println() {

    this.result.add(sb.toString());
    sb.setLength(0);
    
  }

  private StringBuffer sb = new StringBuffer();

  void print(String s) {
    sb.append(s);
  }

}