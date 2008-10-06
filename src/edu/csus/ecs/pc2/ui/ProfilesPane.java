package edu.csus.ecs.pc2.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.csus.ecs.pc2.core.model.Profile;

/**
 * Profile administration pane.
 * 
 * Provides a front end to all profile functions, like rename, change, clone, etc.
 * 
 * 
 * @author pc2@ecs.csus.edu
 * @version $Id$
 */

// $HeadURL$
public class ProfilesPane extends JPanePlugin {

    /**
     * 
     */
    private static final long serialVersionUID = 9075523788534575300L;

    private ProfileSaveFrame profileSaveFrame = null;

    private JLabel profileNameLabel = null;

    private JLabel profileLabel = null;

    private JComboBox profileComboBox = null;

    private JButton switchButton = null;

    private JButton setButton = null;

    private JTextField profileTextField = null;

    private JPanel centerPane = null;

    private JPanel buttonPane = null;

    private JButton newButton = null;

    private JButton exportButton = null;

    private JLabel notificationOfNonImplementationLabel = null;

    private JButton cloneButton = null;

    /**
     * This method initializes
     * 
     */
    public ProfilesPane() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        profileLabel = new JLabel();
        profileLabel.setBounds(new java.awt.Rectangle(26, 88, 94, 23));
        profileLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        profileLabel.setText("Profiles");
        profileNameLabel = new JLabel();
        profileNameLabel.setBounds(new java.awt.Rectangle(14, 40, 106, 22));
        profileNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        profileNameLabel.setText("Profile Name");
        this.setLayout(new BorderLayout());
        this.setSize(new java.awt.Dimension(729, 319));
        this.add(getCenterPane(), java.awt.BorderLayout.CENTER);
        this.add(getButtonPane(), java.awt.BorderLayout.SOUTH);

    }

    public String getPluginTitle() {
        return "Profiles Pane";
    }

    /**
     * This method initializes profileComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getProfileComboBox() {
        if (profileComboBox == null) {
            profileComboBox = new JComboBox();
            profileComboBox.setBounds(new java.awt.Rectangle(134, 85, 339, 28));
        }
        return profileComboBox;
    }

    /**
     * This method initializes Set
     * 
     * @return javax.swing.JButton
     */
    private JButton getSwitchButton() {
        if (switchButton == null) {
            switchButton = new JButton();
            switchButton.setEnabled(true);
            switchButton.setMnemonic(java.awt.event.KeyEvent.VK_W);
            switchButton.setPreferredSize(new java.awt.Dimension(100, 26));
            switchButton.setLocation(new java.awt.Point(497, 85));
            switchButton.setSize(new java.awt.Dimension(100, 28));
            switchButton.setText("Switch");
            switchButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    switchSelectedProfile();
                }
            });
        }
        return switchButton;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getSetButton() {
        if (setButton == null) {
            setButton = new JButton();
            setButton.setEnabled(true);
            setButton.setMnemonic(java.awt.event.KeyEvent.VK_S);
            setButton.setLocation(new java.awt.Point(497, 38));
            setButton.setSize(new java.awt.Dimension(100, 26));
            setButton.setText("Set");
            setButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    renameProfile();
                }
            });
        }
        return setButton;
    }

    /**
     * This method initializes profileTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getProfileTextField() {
        if (profileTextField == null) {
            profileTextField = new JTextField();
            profileTextField.setBounds(new java.awt.Rectangle(134, 36, 339, 30));
        }
        return profileTextField;
    }

    /**
     * This method initializes centerPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCenterPane() {
        if (centerPane == null) {
            notificationOfNonImplementationLabel = new JLabel();
            notificationOfNonImplementationLabel.setBounds(new java.awt.Rectangle(0, 132, 733, 113));
            notificationOfNonImplementationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            notificationOfNonImplementationLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
            notificationOfNonImplementationLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
            notificationOfNonImplementationLabel.setText("<html><center>This pane will allow switching automatically between<br>Practice and Real contest configurations (work-in-progress)</html>");
            centerPane = new JPanel();
            centerPane.setLayout(null);
            centerPane.add(profileNameLabel, null);
            centerPane.add(profileLabel, null);
            centerPane.add(getProfileComboBox(), null);
            centerPane.add(getSwitchButton(), null);
            centerPane.add(getSetButton(), null);
            centerPane.add(getProfileTextField(), null);

            centerPane.add(notificationOfNonImplementationLabel, null);
        }
        return centerPane;
    }

    /**
     * This method initializes buttonPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPane() {
        if (buttonPane == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setHgap(35);
            buttonPane = new JPanel();
            buttonPane.setLayout(flowLayout);
            buttonPane.setPreferredSize(new java.awt.Dimension(35, 35));
            buttonPane.add(getNewButton(), null);
            buttonPane.add(getCloneButton(), null);
            buttonPane.add(getExportButton(), null);
        }
        return buttonPane;
    }

    /**
     * This method initializes newButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getNewButton() {
        if (newButton == null) {
            newButton = new JButton();
            newButton.setText("New");
            newButton.setMnemonic(java.awt.event.KeyEvent.VK_N);
            newButton.setEnabled(true);
            newButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    newProfile();
                }
            });
        }
        return newButton;
    }

    /**
     * This method initializes jButton2
     * 
     * @return javax.swing.JButton
     */
    private JButton getExportButton() {
        if (exportButton == null) {
            exportButton = new JButton();
            exportButton.setText("Export");
            exportButton.setMnemonic(java.awt.event.KeyEvent.VK_X);
            exportButton.setEnabled(true);
            exportButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    exportProfile();
                }
            });
        }
        return exportButton;
    }

    /**
     * This method initializes cloneButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCloneButton() {
        if (cloneButton == null) {
            cloneButton = new JButton();
            cloneButton.setText("Clone");
            cloneButton.setMnemonic(java.awt.event.KeyEvent.VK_C);
            cloneButton.setEnabled(true);
            cloneButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    cloneProfile();
                }
            });
        }
        return cloneButton;
    }

    protected void cloneProfile() {
        getProfileSaveFrame().setTitle("Clone settings for " + getProfileName());
        getProfileSaveFrame().setSaveButtonName(ProfileSavePane.CLONE_BUTTON_NAME);
        getProfileSaveFrame().setVisible(true);
    }

    private String getProfileName() {
        Profile profile = new Profile("Default");
        return profile.getName();
    }

    protected void newProfile() {
        // TODO code newProfile

        showMessage("New Profile, almost coded");
    }

    protected void exportProfile() {
        getProfileSaveFrame().setTitle("Export settings " + getProfileName());
        getProfileSaveFrame().setSaveButtonName(ProfileSavePane.EXPORT_BUTTON_NAME);
        getProfileSaveFrame().setVisible(true);
    }

    protected void switchSelectedProfile() {
        // TODO code switchSelectedProfile

        if (getProfileComboBox().getSelectedIndex() < 0) {
            showMessage("No profile selected");
            return;
        }

        showMessage("Switch Profile, almost coded");
    }

    protected void renameProfile() {
        // TODO code renameProfile

        if (getProfileTextField() == null || getProfileTextField().getText().trim().length() < 1) {
            showMessage("No profile name specified");
            return;
        }

        String newProfileName = getProfileTextField().getText().trim();

        showMessage("Set/Rename Profile, almost coded: " + newProfileName);
    }

    private void showMessage(String string) {
        JOptionPane.showMessageDialog(this, string);
    }

    public ProfileSaveFrame getProfileSaveFrame() {
        if (profileSaveFrame == null) {
            profileSaveFrame = new ProfileSaveFrame();
        }
        return profileSaveFrame;
    }

} // @jve:decl-index=0:visual-constraint="25,9"
