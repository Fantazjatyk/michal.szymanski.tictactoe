/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlaySettings {

    private long timeout;
    private long turnLimit;
    private PlaySettingsSetters setters = new PlaySettingsSetters(this);
    private PlaySettingsGetters getters = new PlaySettingsGetters(this);

    public class PlaySettingsSetters {

        private PlaySettings parent;

        public PlaySettingsSetters(PlaySettings parent) {
            this.parent = parent;
        }

        public PlaySettingsSetters timeout(long timeout, TimeUnit unit) {
            parent.timeout = unit.toMillis(timeout);
            return this;
        }

        public PlaySettingsSetters moveLimit(long timeout, TimeUnit unit) {
            parent.turnLimit = unit.toMillis(timeout);
            return this;
        }
    }

    public class PlaySettingsGetters {

        private PlaySettings parent;

        public PlaySettingsGetters(PlaySettings parent) {
            this.parent = parent;
        }

        public long getTurnLimit() {
            return parent.turnLimit;
        }

        public long getTimeout() {
            return parent.timeout;
        }
    }

    public PlaySettingsGetters getters() {
        return this.getters;
    }

    public PlaySettingsSetters setters() {
        return this.setters;
    }
}
