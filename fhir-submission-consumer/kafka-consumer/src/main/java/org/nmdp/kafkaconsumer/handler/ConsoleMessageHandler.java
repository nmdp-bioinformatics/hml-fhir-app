package org.nmdp.kafkaconsumer.handler;

/**
 * Created by Andrew S. Brown, Ph.D., <andrew@nmdp.org>, on 5/30/17.
 * <p>
 * kafka-consumer
 * Copyright (c) 2012-2017 National Marrow Donor Program (NMDP)
 * <p>
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;  if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 * <p>
 * > http://www.fsf.org/licensing/licenses/lgpl.html
 * > http://www.opensource.org/licenses/lgpl-license.php
 */

import java.io.PrintStream;

public class ConsoleMessageHandler implements KafkaMessageHandler {
    private final PrintStream out;

    public ConsoleMessageHandler() {
        this(System.out);
    }

    public ConsoleMessageHandler(PrintStream out) {
        this.out = out;
    }

    @Override
    public synchronized void process(String topic, int partition, long offset, byte[] key, byte[] payload) throws Exception {
        if (key != null) {
            out.print(topic);
            out.print("-");
            out.print(partition);
            out.print(":");
            out.print(offset);
            out.print(":key:");
            out.write(key);
            out.println();
        }

        if (payload != null) {
            out.print(topic);
            out.print("-");
            out.print(partition);
            out.print(":");
            out.print(offset);
            out.print(":payload:");
            out.write(payload);
            out.println();
        }
        out.flush();
    }

    @Override
    public synchronized void commit(String topic, int partition, long offset) throws Exception {
        out.print(topic);
        out.print("-");
        out.print(partition);
        out.print(":");
        out.print(offset);
        out.println(":commit");
        out.flush();
    }

    @Override
    public synchronized void rollback(String topic, int partition, long offset) throws Exception {
        out.print(topic);
        out.print("-");
        out.print(partition);
        out.print(":");
        out.print(offset);
        out.println(":rollback");
        out.flush();
    }
}
