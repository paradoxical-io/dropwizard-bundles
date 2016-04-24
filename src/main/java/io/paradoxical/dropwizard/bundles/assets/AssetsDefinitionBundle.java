package io.paradoxical.dropwizard.bundles.assets;

import io.dropwizard.assets.AssetsBundle;

public class AssetsDefinitionBundle extends AssetsBundle {
    public AssetsDefinitionBundle(final AssetsDefinition assetsDefinition) {
        super(assetsDefinition.resourcePath(),
              assetsDefinition.uriPath(),
              assetsDefinition.indexFile(),
              assetsDefinition.assetsName());
    }
}
