package it.starksoftware.ssform.model;

import java.util.ArrayList;
import java.util.List;


public class FormElement implements FormObject {

    // different types for the form elements
    public static final int TYPE_EDITTEXT_TEXT_SINGLELINE = 1;
    public static final int TYPE_EDITTEXT_TEXT_MULTILINE = 2;
    public static final int TYPE_EDITTEXT_NUMBER = 3;
    public static final int TYPE_EDITTEXT_EMAIL = 4;
    public static final int TYPE_EDITTEXT_PHONE = 5;
    public static final int TYPE_PICKER_DATE = 6;
    public static final int TYPE_PICKER_TIME = 7;
    public static final int TYPE_SPINNER_DROPDOWN = 8;
    public static final int TYPE_PICKER_MULTI_CHECKBOX = 9;
    public static final int TYPE_EDITTEXT_PASSWORD = 10;
    public static final int TYPE_EDITTEXT_NUMBER_INTEGER = 11;

    private boolean visibility = true;

    // private variables
    private int mTag; // unique tag to identify the object
    private String mAttribute; // unique tag to identify the object
    private int mType; // type for the form element
    private String mTitle; // title to be shown on left
    private String mValue; // value to be shown on right
    private List<String> mOptions; // list of options for single and multi picker
    private List<String> mOptionsSelected; // list of selected options for single and multi picker

    private boolean required = false;
    private String requiredResponseMessage = mTitle;

    public String getRequiredResponseMessage() {
        return requiredResponseMessage;
    }

    public FormElement setRequiredResponseMessage(String requiredResponseMessage) {
        this.requiredResponseMessage = requiredResponseMessage;
        return this;
    }

    public FormElement() {
    }

    public FormElement setVisibility(boolean visibility) {
        this.visibility = visibility;
        return this;
    }
    public boolean getVisibility() {
        return visibility;
    }

    public static FormElement createInstance() {
        return new FormElement();
    }

    // getters and setters
    public FormElement setTag(int mTag) {
        this.mTag = mTag;
        return this;
    }

    public FormElement setType(int mType) {
        this.mType = mType;
        return this;
    }

    public FormElement setAttribute(String attribute) {
        this.mAttribute = attribute;
        return this;
    }

    public String getAttribute() {
        return mAttribute;
    }


    @Override
    public boolean isRequired() {
        return required;
    }

    public FormElement setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public FormElement setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public FormElement setValue(String mValue) {
        this.mValue = mValue;
        return this;
    }

    public FormElement setOptions(List<String> mOptions) {
        this.mOptions = mOptions;
        return this;
    }

    public FormElement setOptionsSelected(List<String> mOptionsSelected) {
        this.mOptionsSelected = mOptionsSelected;
        return this;
    }

    public int getTag() {
        return mTag;
    }


    public int getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getValue() {
        return (mValue == null) ? "" : mValue;
    }

    public List<String> getOptions() {
        return (mOptions == null) ? new ArrayList<String>() : mOptions;
    }

    public List<String> getOptionsSelected() {
        return (mOptionsSelected == null) ? new ArrayList<String>() : mOptionsSelected;
    }



    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getElementType() {

        return "Basic";
    }

    @Override
    public String toString() {
        return "TAG: " + String.valueOf(this.mTag) + ", TITLE: " + this.mTitle + ", VALUE: " + this.mValue ;
    }
}
