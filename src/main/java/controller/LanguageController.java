package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;

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
		Locale.setDefault(language);
	}

	public static List<Locale> getSupportedLanguages() {
		return List.of(Locale.GERMAN, Locale.ENGLISH);
	}

	public static Locale getDefault() {
		return Locale.GERMAN;
	}

	private ResourceBundle getResourceBundleForCurrentLanguage() {
		if (getLanguage().equals(Locale.GERMAN)) {
			return german;
		}
		if (getLanguage().equals(Locale.ENGLISH)) {
			return english;
		}
		throw new IllegalStateException("Language not supported: " + getLanguage());
	}

	private Callable<String> getText(final String key, final Object... args) {
		return () -> MessageFormat.format(getResourceBundleForCurrentLanguage().getString(key), args);
	}

	public StringBinding getStringBinding(final String key) {
		return Bindings.createStringBinding(getText(key), getLanguageProperty());
	}

	public <E> void bindComponent(final E component, final String key) {
		if (component == null) {
			throw new NullPointerException("component is null");
		}
		if (component instanceof Labeled) {
			bindLabeled((Labeled) component, key);
			return;
		}
		if (component instanceof Tab) {
			bindTab((Tab) component, key);
			return;
		}
		if (component instanceof Menu) {
			bindMenu((Menu) component, key);
			return;
		}
		if (component instanceof MenuItem) {
			bindMenuItem((MenuItem) component, key);
			return;
		}
		if (component instanceof TableColumnBase<?,?>) {
			bindTableColumn((TableColumnBase<?,?>) component, key);
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

	private <E extends Menu> void bindMenu(final E menu, final String key) {
		menu.textProperty().bind(getStringBinding(key));
	}

	private <E extends MenuItem> void bindMenuItem(final E menuItem, final String key) {
		menuItem.textProperty().bind(getStringBinding(key));
	}

	private <E extends TableColumnBase<?, ?>> void bindTableColumn(final E tableColumn, final String key) {
		tableColumn.textProperty().bind(getStringBinding(key));
	}
}
