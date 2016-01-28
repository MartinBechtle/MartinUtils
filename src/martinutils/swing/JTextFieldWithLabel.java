package martinutils.swing;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import martinutils.runtime.Assert;

/**
 * A useful class for managing text fields with a label
 * @author martin
 */
public class JTextFieldWithLabel {

	protected String labelValue;
	protected String labelName;
	protected boolean putColon;
	protected JTextField textField;
	protected JLabel label;
	
	/**
	 * Creates a JTextField associated to a label. Use the appendTo(...) method to append it to its parent component
	 * @param label a value for the label
	 * @param putColon if true, semicolon and space is added after the label
	 * @param content optional content for the textfield (can be null)
	 */
	public JTextFieldWithLabel(String label, boolean putColon, String content) {
		
		this.putColon = putColon;
		setLabelValue(label);
		setTextFieldValue(content);
	}
	
	/**
	 * Creates a JTextField associated to a label. Use the appendTo(...) method to append it to its parent component
	 * @param label a value for the label
	 * @param putColon if true, semicolon and space is added after the label
	 */
	public JTextFieldWithLabel(String label, boolean putColon) {
		this(label, putColon, null);
	}
	
	/**
	 * Set the value of the label.
	 * @return this object for method chaining
	 */
	public JTextFieldWithLabel setLabelValue(String value) {
		
		Assert.notNull(value, "value");
		if (this.label == null) {
			this.label = new JLabel();
		}
		this.labelName = value;
		this.label.setText(putColon ? (value + ": ") : value);
		return this;
	}
	
	/**
	 * Get the value of the label
	 * @return
	 */
	public String getLabelValue() {
		return this.label == null ? "" : this.label.getText();
	}
	
	/**
	 * Get the name of the label (which corresponds to the label's value, except for the colon)
	 * @return
	 */
	public String getLabelName() {
		return this.labelName == null ? "" : this.labelName;
	}
	
	/**
	 * Get the embedded JLabel
	 * @return
	 */
	public JLabel getLabel() {
		return this.label;
	}
	
	/**
	 * Get the embedded JTextField
	 * @return
	 */
	public JTextField getTextField() {
		return this.textField;
	}
	
	/**
	 * Set the value for the text field
	 * @return this object for method chaining
	 */
	public JTextFieldWithLabel setTextFieldValue(String value) {
		
		if (this.textField == null) {
			this.textField = new JTextField(value);
		} else {
			this.textField.setText(value);
		}
		return this;
	}
	
	/**
	 * Get the value of the text field
	 * @return
	 */
	public String getTextFieldValue() {
		return this.textField == null ? "" : this.textField.getText();
	}
	
	/**
	 * Append the JLabel and JTextField to a JComponent
	 * @param parent
	 * @return this object for method chaining
	 */
	public JTextFieldWithLabel appendTo(JComponent parent) {
		
		Assert.notNull(parent, "parent");
		parent.add(this.label);
		parent.add(this.textField);
		return this;
	}

	public boolean isPutColon() {
		return putColon;
	}

	public JTextFieldWithLabel setPutColon(boolean putColon) {
		this.putColon = putColon;
		return this;
	}
	
}
