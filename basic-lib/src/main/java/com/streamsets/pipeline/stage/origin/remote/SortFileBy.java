/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.stage.origin.remote;

import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Label;

@GenerateResourceBundle
public enum SortFileBy implements Label {
	NONE("None"),
	FILENAME_ASC("Filename ASC"),
	FILENAME_DESC("Filename DESC"),
	FILECREATETIME_ASC("File Last Mofified Time ASC"),
	FILECREATETIME_DESC("File Last Mofified Time DESC");

	private final String label;

	SortFileBy(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
}
