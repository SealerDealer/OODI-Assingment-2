import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * Main GUI entry point for the Meeting Room Booking System.
 * Course: DRC1213 – Object-Oriented Design & Implementation
 * Phase 3: GUI Swing Implementation
 *
 * OOP Concepts demonstrated:
 *   - Event Driven Programming : ActionListeners on all buttons
 *   - GUI Swing               : JFrame, JPanel, JTable, JButton, JTextField
 *   - Array Storage           : Room[] and Booking[] from BookingSystem (CO3/CO4)
 *   - Polymorphism            : getRoomDetails() called on Room references
 *   - Encapsulation           : All fields private, accessed via getters
 */
public class MainGUI extends JFrame {

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color COL_PRIMARY  = new Color(26,  60, 110);
    private static final Color COL_ACCENT   = new Color(52, 152, 219);
    private static final Color COL_DANGER   = new Color(231, 76,  60);
    private static final Color COL_BG       = new Color(245, 247, 250);
    private static final Color COL_WHITE    = Color.WHITE;
    private static final Color COL_TEXT     = new Color(44,  62,  80);
    private static final Color COL_SUBTEXT  = new Color(127, 140, 141);

    // ── Data ──────────────────────────────────────────────────────────────────
    private BookingSystem system     = new BookingSystem("SYS-001");
    private User          currentUser = null;

    // ── UI Components ─────────────────────────────────────────────────────────
    private JPanel    mainPanel;
    private CardLayout cardLayout;

    // Login
    private JTextField     tfName, tfUserID;
    private JPasswordField tfAdminPw;

    // Room / booking tables
    private JTable             roomTable, bookingTable;
    private DefaultTableModel  roomModel, bookingModel;

    // Booking form
    private JTextField tfDate, tfStart, tfEnd, tfCapacity;
    private JLabel     lblStatus;

    // ─────────────────────────────────────────────────────────────────────────
    public MainGUI() {
        seedData();
        buildFrame();
    }

    // ── Seed rooms ────────────────────────────────────────────────────────────
    private void seedData() {
        system.addRoom(new StandardRoom("R001", 10,  50.0, "Conference", true));
        system.addRoom(new StandardRoom("R002",  6,  30.0, "Discussion", true));
        system.addRoom(new DeluxeRoom  ("R003", 20, 120.0, true,  5));
        system.addRoom(new DeluxeRoom  ("R004", 15,  90.0, false, 3));
        system.addRoom(new StandardRoom("R005",  4,  20.0, "Huddle",    false));
    }

    // ── Frame ─────────────────────────────────────────────────────────────────
    private void buildFrame() {
        setTitle("Meeting Room Booking System – DRC1213");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 660);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(COL_BG);

        mainPanel.add(buildLoginPanel(),   "LOGIN");
        mainPanel.add(buildUserPanel(),    "USER");
        mainPanel.add(buildAdminPanel(),   "ADMIN");
        mainPanel.add(buildBookingPanel(), "BOOK");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // LOGIN PANEL  (User Login + Admin Login tabs — no Register)
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(COL_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COL_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        card.setPreferredSize(new Dimension(420, 420));

        JLabel title = new JLabel("Meeting Room Booking");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(COL_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("DRC1213 – OOP Implementation");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(COL_SUBTEXT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("User Login",  buildUserLoginTab());
        tabs.addTab("Admin Login", buildAdminLoginTab());

        card.add(tabs);
        outer.add(card);
        return outer;
    }

    private JPanel buildUserLoginTab() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COL_WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        GridBagConstraints g = gbc();

        tfName   = styledField("Enter your full name");
        tfUserID = styledField("Enter your student/staff ID");

        g.gridy = 0; p.add(label("Full Name"), g);
        g.gridy = 1; p.add(tfName,             g);
        g.gridy = 2; p.add(label("User ID"),   g);
        g.gridy = 3; p.add(tfUserID,           g);
        g.gridy = 4;
        JButton btn = primaryBtn("Login");
        btn.addActionListener(e -> doUserLogin());
        p.add(btn, g);
        return p;
    }

    private JPanel buildAdminLoginTab() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COL_WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        GridBagConstraints g = gbc();

        tfAdminPw = new JPasswordField();
        tfAdminPw.setFont(new Font("Arial", Font.PLAIN, 13));
        tfAdminPw.setPreferredSize(new Dimension(280, 36));
        tfAdminPw.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        g.gridy = 0; p.add(label("Admin Password"), g);
        g.gridy = 1; p.add(tfAdminPw,               g);
        g.gridy = 2;
        JButton btn = primaryBtn("Admin Login");
        btn.addActionListener(e -> doAdminLogin());
        p.add(btn, g);
        return p;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // USER PANEL
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildUserPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(COL_BG);
        outer.add(topBar("User Dashboard", true), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabs.addTab("Available Rooms", buildAvailableRoomsTab());
        tabs.addTab("My Bookings",     buildMyBookingsTab());

        outer.add(tabs, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildAvailableRoomsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(COL_BG);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Room ID", "Type", "Capacity", "Price / hr", "Status"};
        roomModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        roomTable = new JTable(roomModel);
        styleTable(roomTable);
        refreshRoomTable();

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(COL_BG);
        JButton btnBook    = primaryBtn("Book Selected Room");
        JButton btnDetails = secondaryBtn("View Details");
        JButton btnRefresh = secondaryBtn("Refresh");
        btnBook.addActionListener(e    -> openBookingForm());
        btnDetails.addActionListener(e -> viewRoomDetails());
        btnRefresh.addActionListener(e -> refreshRoomTable());
        btns.add(btnBook); btns.add(btnDetails); btns.add(btnRefresh);

        p.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildMyBookingsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(COL_BG);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Booking ID", "Room", "Date", "Start", "End", "Price", "Status"};
        bookingModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bookingTable = new JTable(bookingModel);
        styleTable(bookingTable);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(COL_BG);
        JButton btnCancel  = dangerBtn("Cancel Selected Booking");
        JButton btnRefresh = secondaryBtn("Refresh");
        btnCancel.addActionListener(e  -> cancelBooking());
        btnRefresh.addActionListener(e -> refreshBookingTable());
        btns.add(btnCancel); btns.add(btnRefresh);

        p.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // BOOKING FORM PANEL
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildBookingPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(COL_BG);
        outer.add(topBar("Make a Booking", true), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(COL_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COL_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        card.setPreferredSize(new Dimension(500, 440));

        JLabel title = new JLabel("Book a Meeting Room");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(COL_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(8));

        JLabel hint = new JLabel("Max booking duration: 4 hours");
        hint.setFont(new Font("Arial", Font.PLAIN, 11));
        hint.setForeground(COL_SUBTEXT);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(hint);
        card.add(Box.createVerticalStrut(22));

        tfDate     = styledField("DD-MM-YYYY  e.g. 15-06-2026");
        tfStart    = styledField("HH:MM  e.g. 09:00");
        tfEnd      = styledField("HH:MM  e.g. 11:00");
        tfCapacity = styledField("e.g. 8");

        card.add(formRow("Date",              tfDate));
        card.add(Box.createVerticalStrut(10));
        card.add(formRow("Start Time",        tfStart));
        card.add(Box.createVerticalStrut(10));
        card.add(formRow("End Time",          tfEnd));
        card.add(Box.createVerticalStrut(10));
        card.add(formRow("Min. Capacity",     tfCapacity));
        card.add(Box.createVerticalStrut(20));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblStatus);
        card.add(Box.createVerticalStrut(10));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setBackground(COL_WHITE);
        btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton btnSearch = primaryBtn("Search Available Rooms");
        JButton btnBack   = secondaryBtn("Back");
        btnSearch.addActionListener(e -> searchAndBook());
        btnBack.addActionListener(e   -> cardLayout.show(mainPanel, "USER"));
        btnRow.add(btnSearch); btnRow.add(btnBack);
        card.add(btnRow);

        center.add(card);
        outer.add(center, BorderLayout.CENTER);
        return outer;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ADMIN PANEL
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildAdminPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(COL_BG);
        outer.add(topBar("Admin Dashboard", true), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabs.addTab("All Rooms",    buildAllRoomsAdminTab());
        tabs.addTab("Usage Report", buildReportTab());

        outer.add(tabs, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildAllRoomsAdminTab() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(COL_BG);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Room ID", "Type", "Capacity", "Price / hr", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        for (Room r : system.getRoomList()) {
            String type = (r instanceof DeluxeRoom) ? "Deluxe" : "Standard";
            model.addRow(new Object[]{
                r.getRoomID(), type, r.getCapacity(),
                String.format("RM %.2f", r.getPricePerHour()),
                r.isAvailable() ? "Available" : "Unavailable"
            });
        }
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildReportTab() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(COL_BG);
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea ta = new JTextArea();
        ta.setFont(new Font("Courier New", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(new Color(30, 30, 30));
        ta.setForeground(new Color(0, 230, 120));
        ta.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        ta.setText("Click 'Generate Report' to view usage statistics.");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(COL_BG);
        JButton btnGen = primaryBtn("Generate Report");
        btnGen.addActionListener(e -> {
            Room[] rooms = system.getRoomList();
            int avail = 0;
            for (Room r : rooms) if (r.isAvailable()) avail++;
            StringBuilder sb = new StringBuilder();
            sb.append("========== USAGE REPORT ==========\n");
            sb.append("System ID       : SYS-001\n");
            sb.append("Total Rooms     : ").append(rooms.length).append("\n");
            sb.append("Available       : ").append(avail).append("\n");
            sb.append("Unavailable     : ").append(rooms.length - avail).append("\n\n");
            sb.append("--- ROOM LIST ---\n");
            for (Room r : rooms) {
                String t = (r instanceof DeluxeRoom) ? "DELUXE  " : "STANDARD";
                sb.append(String.format("%-6s | %s | Cap:%-3d | RM%.2f/hr | %s\n",
                    r.getRoomID(), t, r.getCapacity(), r.getPricePerHour(),
                    r.isAvailable() ? "AVAILABLE" : "UNAVAILABLE"));
            }
            sb.append("==================================\n");
            ta.setText(sb.toString());
        });
        btns.add(btnGen);

        p.add(new JScrollPane(ta), BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // EVENT HANDLERS
    // ═════════════════════════════════════════════════════════════════════════
    private void doUserLogin() {
        String name = fieldVal(tfName,   "Enter your full name");
        String uid  = fieldVal(tfUserID, "Enter your student/staff ID");
        if (name.isEmpty() || uid.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter your name and User ID.", "Login Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        currentUser = new User(uid, name, uid + "@ump.edu.my");
        JOptionPane.showMessageDialog(this,
            "Welcome, " + name + "!", "Login Successful",
            JOptionPane.INFORMATION_MESSAGE);
        refreshRoomTable();
        cardLayout.show(mainPanel, "USER");
    }

    private void doAdminLogin() {
        String pw = new String(tfAdminPw.getPassword()).trim();
        if (pw.equals("admin123")) {
            cardLayout.show(mainPanel, "ADMIN");
        } else {
            JOptionPane.showMessageDialog(this,
                "Incorrect password.", "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openBookingForm() {
        if (currentUser == null) return;
        lblStatus.setText(" ");
        tfDate.setText(""); tfStart.setText("");
        tfEnd.setText(""); tfCapacity.setText("");
        cardLayout.show(mainPanel, "BOOK");
    }

    private void searchAndBook() {
        String date  = tfDate.getText().trim();
        String start = tfStart.getText().trim();
        String end   = tfEnd.getText().trim();
        String capS  = tfCapacity.getText().trim();

        if (date.isEmpty() || start.isEmpty() || end.isEmpty() || capS.isEmpty()) {
            setStatus("Please fill in all fields.", true);
            return;
        }
        int cap;
        try { cap = Integer.parseInt(capS); }
        catch (NumberFormatException ex) { setStatus("Capacity must be a number.", true); return; }

        Room[] found = system.searchRooms(date, start, cap);
        if (found.length == 0) {
            setStatus("No rooms match your criteria. Try different time or capacity.", true);
            return;
        }

        String[] options = new String[found.length];
        for (int i = 0; i < found.length; i++) {
            String type = (found[i] instanceof DeluxeRoom) ? "Deluxe" : "Standard";
            options[i] = found[i].getRoomID() + "  |  " + type
                + "  |  Capacity: " + found[i].getCapacity()
                + "  |  RM" + String.format("%.2f", found[i].getPricePerHour()) + "/hr";
        }

        String chosen = (String) JOptionPane.showInputDialog(
            this, "Select a room:", "Available Rooms",
            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (chosen == null) return;

        String roomID = chosen.split("\\|")[0].trim();
        Room room = system.findRoomByID(roomID);

        Booking booking = system.createBooking(currentUser, room, date, start, end);
        if (booking == null) {
            setStatus("Booking failed. Check time range (max 4 hrs).", true);
            return;
        }

        // Add to booking table
        if (bookingModel != null) {
            bookingModel.addRow(new Object[]{
                booking.getBookingID(),
                room.getRoomID(),
                date, start, end,
                String.format("RM %.2f", booking.getTotalPrice()),
                "ACTIVE"
            });
        }
        refreshRoomTable();

        JOptionPane.showMessageDialog(this,
            "Booking Confirmed!\n\n"
            + "Booking ID : " + booking.getBookingID() + "\n"
            + "Room       : " + room.getRoomID() + "\n"
            + "Date       : " + date + "\n"
            + "Time       : " + start + " to " + end + "\n"
            + "Total      : RM" + String.format("%.2f", booking.getTotalPrice()),
            "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

        cardLayout.show(mainPanel, "USER");
    }

    private void cancelBooking() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a booking to cancel.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String bid = (String) bookingModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel booking " + bid + "?", "Confirm",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = system.cancelBooking(bid);
            if (ok) {
                bookingModel.setValueAt("CANCELLED", row, 6);
                refreshRoomTable();
                JOptionPane.showMessageDialog(this,
                    "Booking " + bid + " cancelled.", "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void viewRoomDetails() {
        int row = roomTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a room first.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String roomID = (String) roomModel.getValueAt(row, 0);
        Room r = system.findRoomByID(roomID);
        if (r != null) {
            // Polymorphism: correct getRoomDetails() called based on actual type
            JOptionPane.showMessageDialog(this, r.getRoomDetails(),
                "Room Details – " + roomID, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════
    private void refreshRoomTable() {
        if (roomModel == null) return;
        roomModel.setRowCount(0);
        for (Room r : system.getRoomList()) {
            String type   = (r instanceof DeluxeRoom) ? "Deluxe" : "Standard";
            String status = r.isAvailable() ? "Available" : "Unavailable";
            roomModel.addRow(new Object[]{
                r.getRoomID(), type, r.getCapacity(),
                String.format("RM %.2f", r.getPricePerHour()),
                status
            });
        }
    }

    private void refreshBookingTable() {
        // bookings are added live in searchAndBook(); nothing extra needed
    }

    private void setStatus(String msg, boolean error) {
        lblStatus.setForeground(error ? COL_DANGER : new Color(39, 174, 96));
        lblStatus.setText(msg);
    }

    private String fieldVal(JTextField f, String placeholder) {
        String v = f.getText().trim();
        return v.equals(placeholder) ? "" : v;
    }

    // ── Top bar ───────────────────────────────────────────────────────────────
    private JPanel topBar(String title, boolean showLogout) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(COL_PRIMARY);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        bar.setPreferredSize(new Dimension(0, 55));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(COL_WHITE);
        bar.add(lbl, BorderLayout.WEST);

        if (showLogout) {
            JButton btn = new JButton("Logout");
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setForeground(COL_PRIMARY);
            btn.setBackground(COL_WHITE);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COL_WHITE, 1, true),
                BorderFactory.createEmptyBorder(4, 14, 4, 14)
            ));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> { currentUser = null; cardLayout.show(mainPanel, "LOGIN"); });
            bar.add(btn, BorderLayout.EAST);
        }
        return bar;
    }

    // ── Form row ──────────────────────────────────────────────────────────────
    private JPanel formRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(COL_WHITE);
        row.setMaximumSize(new Dimension(440, 65));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(COL_TEXT);
        lbl.setPreferredSize(new Dimension(130, 36));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    // ── Styled text field ─────────────────────────────────────────────────────
    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(280, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        f.setForeground(COL_SUBTEXT);
        f.setText(placeholder);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(COL_TEXT); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(COL_SUBTEXT); }
            }
        });
        return f;
    }

    // ── Label ─────────────────────────────────────────────────────────────────
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(COL_TEXT);
        return l;
    }

    // ── GridBagConstraints default ────────────────────────────────────────────
    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.gridx  = 0;
        return g;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────
    private JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(COL_ACCENT);
        b.setForeground(Color.BLACK);         
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    private JButton secondaryBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.PLAIN, 13));
        b.setBackground(new Color(220, 220, 220));
        b.setForeground(Color.BLACK);          // dark text
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    private JButton dangerBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(COL_DANGER);
        b.setForeground(Color.BLACK);          // dark text
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    // ── Table styling ─────────────────────────────────────────────────────────
    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(52, 152, 219, 80));
        table.setSelectionForeground(COL_TEXT);
        table.setBackground(COL_WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(COL_PRIMARY);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 36));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                lbl.setBackground(COL_PRIMARY);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(new Font("Arial", Font.BOLD, 13));
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return lbl;
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? COL_WHITE : new Color(248, 249, 250));
                setForeground(COL_TEXT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }

    // ═════════════════════════════════════════════════════════════════════════
    // MAIN
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainGUI::new);
    }
}