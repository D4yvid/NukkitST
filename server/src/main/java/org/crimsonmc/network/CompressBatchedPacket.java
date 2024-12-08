package org.crimsonmc.network;

import org.crimsonmc.network.binary.ZlibCompression;
import org.crimsonmc.scheduler.AsyncTask;
import org.crimsonmc.server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * author: MagicDroidX crimsonmc Project
 */
public class CompressBatchedPacket extends AsyncTask {

    public int level = 7;

    public byte[] data;

    public byte[] finalData;

    public int channel = 0;

    public List<String> targets = new ArrayList<>();

    public CompressBatchedPacket(byte[] data, List<String> targets) {
        this(data, targets, 7);
    }

    public CompressBatchedPacket(byte[] data, List<String> targets, int level) {
        this(data, targets, level, 0);
    }

    public CompressBatchedPacket(byte[] data, List<String> targets, int level, int channel) {
        this.data = data;
        this.targets = targets;
        this.level = level;
        this.channel = channel;
    }

    @Override
    public void onRun() {
        try {
            this.finalData = ZlibCompression.deflate(data, level);
            this.data = null;
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void onCompletion(Server server) {
        server.broadcastPacketsCallback(this.finalData, this.targets);
    }
}
