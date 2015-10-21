/**
 * @author Petr (http://www.sallyx.org/)
 */
package Pathfinder;

import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JCheckBoxMenuItem;
import static Pathfinder.resource.*;
import javax.swing.MenuElement;
import static common.windows.*;

public class toolbar {

    public static class MyMenuBar extends JMenuBar {

        final private ActionListener al;
        private Map<Integer, MyCheckBoxMenuItem> items = new HashMap<Integer, MyCheckBoxMenuItem>();

        public MyMenuBar() {
            super();
            al = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    MyMenuItem source = (MyMenuItem) e.getSource();
                    Main.HandleMenuItems(source.getID(), MyMenuBar.this);
                }
            };
        }

        @Override
        public JMenu add(JMenu c) {
            for (MenuElement elm : c.getSubElements()) {
                for (MenuElement comp : elm.getSubElements()) {
                    if (comp instanceof MyCheckBoxMenuItem) {
                        MyCheckBoxMenuItem myelm = (MyCheckBoxMenuItem) comp;
                        this.items.put(myelm.getID(), myelm);
                    }
                }
            }
            return super.add(c);
        }

        private ActionListener getActionListener() {
            return al;
        }

        /**
         * Swap menu state and do call actionEvent
         * 
         * @param MenuItem ID of MyCheckBoxMenuItem
         */
        public void changeMenuState(int MenuItem) {
            MyCheckBoxMenuItem item = this.items.get(MenuItem);
            if (item != null) {
                item.doClick();
            }
        }

        /**
         * Set menu state and do not call actionEvent
         * 
         * @param MenuItem ID of MyCheckBoxMenuItem
         * @param state New state (MFS_CHECKED or MFS_UNCHECKED)
         */
        public void setMenuState(int MenuItem, final long state) {
            MyCheckBoxMenuItem item = this.items.get(MenuItem);
            if (item == null) {
                return;
            }
            if (state == MFS_CHECKED) {
                item.setSelected(true);
            } else if (state == MFS_UNCHECKED) {
                item.setSelected(false);
            } else {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }

    public static interface MyMenuItem {

        public int getID();
    }

    public static class MyButtonMenuItem extends JMenuItem implements MyMenuItem {

        private final int id;

        public MyButtonMenuItem(String title, int id, ActionListener al) {
            super(title);
            this.id = id;
            this.addActionListener(al);
        }

        @Override
        public int getID() {
            return id;
        }
    }

    public static class MyCheckBoxMenuItem extends JCheckBoxMenuItem implements MyMenuItem {

        private final int id;

        public MyCheckBoxMenuItem(String title, int id, ActionListener al) {
            this(title, id, al, false);
        }

        public MyCheckBoxMenuItem(String title, int id, ActionListener al, boolean checked) {
            super(title, checked);
            this.id = id;
            this.addActionListener(al);
        }

        @Override
        public int getID() {
            return id;
        }
    }

    public static MyMenuBar createMenu(final int id_menu) {
        MyMenuBar menu = new MyMenuBar();
        ActionListener al = menu.getActionListener();
        JMenu file = new JMenu("File");
        JMenuItem saveAs = new MyButtonMenuItem("Save as", ID_MENU_SAVEAS, al);
        JMenuItem load = new MyButtonMenuItem("Load", ID_MENU_LOAD, al);
        JMenuItem novy = new MyButtonMenuItem("New", ID_MENU_NEW, al);

        file.add(saveAs);
        file.add(load);
        file.add(novy);

        JMenu view = new JMenu("View");
        JMenuItem graph = new MyCheckBoxMenuItem("Graph", IDM_VIEW_GRAPH, al);
        JMenuItem tiles = new MyCheckBoxMenuItem("Tiles", IDM_VIEW_TILES, al);

        view.add(graph);
        view.add(tiles);

        menu.add(file);
        menu.add(view);

        return menu;
    }
}