import java.io.{ FileOutputStream, File }
import java.net.{ InetAddress, InetSocketAddress }
import com.turn.ttorrent.client._
import com.turn.ttorrent.common.Torrent
import com.turn.ttorrent.tracker._
import java.nio.file.{ Files, Paths }
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import collection.JavaConversions._
/*
 * Copyright (C) 2015 Romain Reuillon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

object Track extends App {

  val torrentFile = new File("/tmp/test.torrent")

  val tracker = new Tracker(new InetSocketAddress(InetAddress.getLoopbackAddress, 6969))

  val dir = new File("/home/reuillon/bin")
  val torrent = Torrent.create(new File(dir, "care"), tracker.getAnnounceUrl().toURI, "romain")

  val fos = new FileOutputStream(torrentFile)
  try torrent.save(fos)
  finally fos.close()

  val trackedTorrent = new TrackedTorrent(torrent)
  tracker.announce(trackedTorrent)

  val f = Future { tracker.start }

  val client = new Client(
    InetAddress.getLoopbackAddress,
    new SharedTorrent(torrent, dir, true)
  )

  val stop = client.getClass.getDeclaredField("stop")
  stop.setAccessible(true)
  stop.set(client, false)

  val seed = client.getClass.getDeclaredField("seed")
  seed.setAccessible(true)
  seed.set(client, true)

  client.run()

}

