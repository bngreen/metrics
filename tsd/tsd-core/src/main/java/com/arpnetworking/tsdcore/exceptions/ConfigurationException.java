/**
 * Copyright 2014 Brandon Arp
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
package com.arpnetworking.tsdcore.exceptions;

/**
 * Exception thrown when configuration cannot be parsed.
 *
 * @author Brandon Arp (barp at groupon dot com)
 */
public class ConfigurationException extends Exception {

    /**
     * Public constructor with a description.
     * 
     * @param message Describes the exceptional condition.
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Public constructor with a description and cause.
     * 
     * @param message Describes the exceptional condition.
     * @param cause Causing exception.
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    private static final long serialVersionUID = -3566846577077218388L;
}
