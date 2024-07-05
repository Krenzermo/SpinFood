package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class LanguageController {
	private static LanguageController instance;
	private static final ObjectProperty<Locale> language = new SimpleObjectProperty<>(getDefault());
	private final ResourceBundle german;
	private final ResourceBundle english;

	private LanguageController() {
		instance = this;
		Locale.setDefault(getDefault());
		german = ResourceBundle.getBundle("application", Locale.GERMAN);
		english = ResourceBundle.getBundle("application", Locale.ENGLISH);
	}

	public static LanguageController getInstance() {
		if (instance == null) {
			instance = new LanguageController();
		}
		return instance;
	}

	private ObjectProperty<Locale> getLanguageProperty() {
		return language;
	}

	public Locale getLanguage() {
		return getLanguageProperty().get();
	}

	public void setLanguage(Locale language) {
		if (!getSupportedLanguages().contains(language)) {
			throw new IllegalArgumentException("Language not supported: " + language);
		}
		getLanguageProperty().set(language);
		Locale.setDefault(language); // changes language of default Buttons (CLOSE, APPLY, ...)
	}

	public static List<Locale> getSupportedLanguages() {
		return List.of(Locale.GERMAN, Locale.ENGLISH);
	}

	public static Locale getDefault() {
		return Locale.GERMAN;
	}

	private ResourceBundle getResourceBundleForCurrentLanguage() {
		return getResourceBundleForLanguage(getLanguage());
	}

	private ResourceBundle getResourceBundleForLanguage(Locale locale) {
		if (locale.equals(Locale.GERMAN)) {
			return german;
		}
		if (locale.equals(Locale.ENGLISH)) {
			return english;
		}
		throw new IllegalArgumentException("Language not supported: " + getLanguage());
	}

	/**
	 * This method retrieves the text associated with the given key from the ResourceBundle.
	 * Use this method to retrieve header and title texts.
	 * (They cannot be changed during the runtime by establishing a StringBinding)
	 *
	 * @param key the key to retrieve the associated text from the ResourceBundle
	 * @return the text associated with the given key in ResourceBundle
	 */
	public String getText(String key) {
		return getResourceBundleForCurrentLanguage().getString(key);
	}

	public String getText(String key, Locale locale) {
		return getResourceBundleForLanguage(locale).getString(key);
	}

	private Callable<String> getTextInternal(final String key, final Object... args) {
		return () -> MessageFormat.format(getResourceBundleForCurrentLanguage().getString(key), args);
	}

	public StringBinding getStringBinding(final String key) {
		return Bindings.createStringBinding(getTextInternal(key), getLanguageProperty());
	}

	public void attachChangeListener(javafx.beans.value.ChangeListener<? super Locale> listener) {
		getLanguageProperty().addListener(listener);
	}

	public <E> void bindComponent(final E component, final String key) {
		if (component == null) {
			throw new NullPointerException("component is null");
		}
		if (component instanceof Labeled) { // a lot of things are instances of Labeled
			bindLabeled((Labeled) component, key);
			return;
		}
		if (component instanceof Tab) {
			bindTab((Tab) component, key);
			return;
		}
		if (component instanceof MenuItem) { // Menu is instance of MenuItem
			bindMenuItem((MenuItem) component, key);
			return;
		}
		if (component instanceof TableColumnBase<?,?>) {
			bindTableColumn((TableColumnBase<?,?>) component, key);
			return;
		}
		if (component instanceof TableView<?>) {
			bindTableDefaultText((TableView<?>) component, key);
			return;
		}
		if (component instanceof TextInputControl) {
			bindTextField((TextInputControl) component, key);
			return;
		}
		if (component instanceof Text) {
			bindText((Text) component, key);
			return;
		}
		if (component instanceof Dialog<?>) {
			bindDialog((Dialog<?>) component, key);
			return;
		}
		if (component instanceof DialogPane) {
			bindDialogPaneHeaderText((DialogPane) component, key);
			return;
		}
		if (component instanceof Stage) {
			bindStage((Stage) component, key);
			return;
		}
		throw new UnsupportedOperationException("Component not supported yet: " + component);
	}

	private <E extends Labeled> void bindLabeled(final E labeled, final String key) {
		labeled.textProperty().bind(getStringBinding(key));
	}


	private <E extends Tab> void bindTab(final E tab, final String key) {
		tab.textProperty().bind(getStringBinding(key));
	}

	private <E extends MenuItem> void bindMenuItem(final E menuItem, final String key) {
		menuItem.textProperty().bind(getStringBinding(key));
	}

	private <E extends TableColumnBase<?, ?>> void bindTableColumn(final E tableColumn, final String key) {
		tableColumn.textProperty().bind(getStringBinding(key));
	}

	private <E extends TableView<?>> void bindTableDefaultText(final E table, final String key) {
		Label label = new Label();
		label.textProperty().bind(getStringBinding(key));
		table.setPlaceholder(label);
	}

	private <E extends TextInputControl> void bindTextField(final E textInputControl, final String key) {
		textInputControl.textProperty().bind(getStringBinding(key));
	}

	private <E extends Text> void bindText(final E text, final String key) {
		text.textProperty().bind(getStringBinding(key));
	}

	private <E extends Dialog<?>> void bindDialog(final E dialog, final String key) {
		dialog.titleProperty().bind(getStringBinding(key));
	}

	private <E extends DialogPane> void bindDialogPaneHeaderText(final E dialogPane, final String key) {
		dialogPane.headerTextProperty().bind(getStringBinding(key));
	}

	private <E extends Stage> void bindStage(final E stage, final String key) {
		stage.titleProperty().bind(getStringBinding(key));
	}
}
