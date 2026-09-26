import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

case class BankTransaction(transactionId: String, accountId: String, timestamp: String, amount: Double, branchId: String, transactionType: String)
case class BranchRisk(branchName: String, riskLevel: String)

object Day27 {
  private val Port = 9998

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Day 27 - Real-Time Banking Project").setMaster("local[4]")
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint("output/checkpoint")
    ssc.sparkContext.setLogLevel("WARN")

    val branchRiskReference = Map(
      "B001" -> BranchRisk("Hyderabad Central", "HIGH"),
      "B002" -> BranchRisk("Warangal", "LOW"),
      "B003" -> BranchRisk("Vijayawada", "MEDIUM")
    )
    val branchRiskBroadcast = ssc.sparkContext.broadcast(branchRiskReference)
    val lines = ssc.socketTextStream("localhost", Port)

    val transactions = lines.flatMap { line =>
      val p = line.split(",").map(_.trim)
      if (p.length == 6) {
        try Some(BankTransaction(p(0), p(1), p(2), p(3).toDouble, p(4), p(5)))
        catch { case _: NumberFormatException => None }
      } else None
    }.persist(StorageLevel.MEMORY_ONLY)

    val accountTotals = transactions.map(t => (t.accountId, t.amount)).reduceByKey(_ + _)
    accountTotals.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println("\n----------------------------------------------")
        println("ACCOUNT TOTALS - " + time)
        rdd.sortByKey().collect().foreach { case (account, total) =>
          println(f"$account -> $total%.2f")
        }
        println("----------------------------------------------")
      }
    }

    val suspiciousBursts = transactions
      .map(t => (t.accountId, 1))
      .reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(20), Seconds(10))
      .filter { case (_, count) => count >= 3 }

    suspiciousBursts.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println("\n----------------------------------------------")
        println("SUSPICIOUS BURST ALERT - " + time)
        rdd.sortByKey().collect().foreach { case (account, count) =>
          println("BURST ALERT: " + account + " -> " + count + " transactions in window")
        }
        println("----------------------------------------------")
      }
    }

    val branchRiskPair = ssc.sparkContext
      .parallelize(branchRiskBroadcast.value.toSeq)
      .partitionBy(new HashPartitioner(4))
      .persist(StorageLevel.MEMORY_ONLY)

    val enrichedTransactions = transactions.transform { rdd =>
      val transactionPairs = rdd.map(t => (t.branchId, t)).partitionBy(new HashPartitioner(4))
      transactionPairs.join(branchRiskPair).map {
        case (branchId, (tx, risk)) => (branchId, tx, risk)
      }
    }

    enrichedTransactions.foreachRDD { (rdd, time) =>
      if (!rdd.isEmpty()) {
        println("\n----------------------------------------------")
        println("BRANCH/RISK ENRICHMENT - " + time)
        rdd.collect().sortBy(_._2.transactionId).foreach { case (branchId, tx, risk) =>
          val amount = "%.2f".format(tx.amount)
          println(tx.transactionId + " | account=" + tx.accountId + " | amount=" + amount +
            " | branch=" + branchId + " (" + risk.branchName + ") | risk=" + risk.riskLevel)
        }
        println("----------------------------------------------")
      }
    }

    println("==============================================")
    println("DAY 27 - REAL-TIME BANKING PROJECT")
    println("==============================================")
    println("Batch interval : 5 seconds")
    println("Window size    : 20 seconds")
    println("Slide interval : 10 seconds")
    println("Socket server  : localhost:" + Port)
    println("Suspicious burst threshold: >= 3 transactions/account/window")
    println()

    ssc.start()
    println("StreamingContext started.")
    println("Send banking events to port " + Port + ".")
    println("Press Ctrl+C to stop.")
    ssc.awaitTermination()
  }
}
