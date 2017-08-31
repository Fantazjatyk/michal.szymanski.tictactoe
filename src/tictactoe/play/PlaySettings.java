/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.play;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlaySettings {

    private long timeout;
    private long turnLimit;
    private boolean beginOnAllPlayersJoined = false;

    private PlaySettingsSetters setters = new PlaySettingsSetters(this);
    private PlaySettingsGetters getters = new PlaySettingsGetters(this);

    public class PlaySettingsSetters {

        private PlaySettings parent;

        public PlaySettingsSetters(PlaySettings parent) {
            this.parent = parent;
        }

        public PlaySettingsSetters gameTimeLimit(long timeout, TimeUnit unit) {
            parent.timeout = unit.toMillis(timeout);
            return this;
        }

        public PlaySettingsSetters moveTimeLimit(long timeout, TimeUnit unit) {
            parent.turnLimit = unit.toMillis(timeout);
            return this;
        }

        public PlaySettingsSetters beginOnAllPlayersJoined() {
            parent.beginOnAllPlayersJoined = true;
            return this;
        }
    }

    public class PlaySettingsGetters {

        private PlaySettings parent;

        public PlaySettingsGetters(PlaySettings parent) {
            this.parent = parent;
        }

        public long getTurnTimeLimitInMilis() {
            return parent.turnLimit;
        }

        public long getGameTimeLimitInMilis() {
            return parent.timeout;
        }

        public boolean getBeginOnAllPlayersJoined() {
            return parent.beginOnAllPlayersJoined;
        }
    }

    public PlaySettingsGetters getters() {
        return this.getters;
    }

    public PlaySettingsSetters setters() {
        return this.setters;
    }
}
