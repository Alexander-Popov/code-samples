<?php
declare(strict_types = 1);

namespace app\Core\Infrastructure\Repository;

interface HydratorInterface
{
    /**
     * Return some entity
     *
     * @param array $columns
     */
    public function hydrate(array $columns);

    /**
     * @param $entity
     * @return array
     */
    public function extract($entity) : array;
}