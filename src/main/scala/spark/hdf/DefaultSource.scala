/*
 *
 * Copyright (c) 2016, Lawrence Livermore National Security, LLC.
 * Produced at the Lawrence Livermore National Laboratory
 *
 * Written by Joshua Asplund <asplund1@llnl.gov>
 * LLNL-CODE-699384
 *
 * All rights reserved.
 *
 * This file is part of spark-hdf5.
 * For details, see https://github.com/LLNL/spark-hdf5
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package spark.hdf

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources._

class DefaultSource extends RelationProvider {

  // RelationProvider Trait
  override def createRelation(sqlContext: SQLContext,
                              parameters: Map[String, String]): BaseRelation = {

    val paths = parameters.get("path") match {
      case Some(x) => x.split(",").map(_.trim)
      case None => sys.error("'path' must be specified.")
    }

    val extensions = parameters.getOrElse("extension", "h5").split(",").map(_.trim)

    val dataset = parameters.getOrElse("dataset", "/")

    val chunkSize = parameters.getOrElse("chunk size", "10000").toInt

    new HDF5Relation(paths, dataset, extensions, chunkSize)(sqlContext)

  }

}
