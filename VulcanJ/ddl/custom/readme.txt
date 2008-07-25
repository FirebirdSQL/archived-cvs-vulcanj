tests in this directory represent sas-specific vulcan customizations
and aren't expected to pass against SF vulcan.

to date, these include:
- the SAS Vulcan implmentation of UTF16/UTF32 (to be replaced at a later date by ICU)
- Table Coupling, allowing the user to attach a password to the database file for embedded mode
- RETURNING clause, allowing bookmark/rowID to be returned
